/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.holidays.internal.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openhab.binding.holidays.internal.EventManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Renner
 *
 */
public class SchoolHolidaysManager {

    private static final Logger logger = LoggerFactory.getLogger(SchoolHolidaysManager.class);
    private final EventManager eventManager;
    private final File holidaysFile;
    private Set<LocalDate> vacations = Collections.emptySet();
    private long lastRead = -1;

    public SchoolHolidaysManager(EventManager eventManager, String holidaysFile) {
        this.eventManager = eventManager;
        this.holidaysFile = new File(holidaysFile);
        // File has to exist.
        if (!this.holidaysFile.exists()) {
            logger.warn("Vacation file {} does not exist", this.holidaysFile.getAbsolutePath());
        }

        // Trigger initial reading of the file
        readFileIfChanged();
    }

    public boolean isSchoolHoliday(LocalDate date) {
        synchronized (this) {
            return vacations.contains(date);
        }
    }

    public void readFileIfChanged() {
        if (!holidaysFile.exists()) {
            return;
        }

        long modificationTime = holidaysFile.lastModified();
        synchronized (this) {
            if (modificationTime > lastRead) {
                readFile();
                eventManager.fireEvents();
            }
        }
    }

    /**
     * Reads the vacation file into the set and updates the timestamp "lastRead".
     *
     * If the file contains ranges, each day of the range is included in the set.
     *
     * The file has to exist.
     */
    private void readFile() {
        SortedSet<LocalDate> localVacations = new TreeSet<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        try (LineNumberReader lnr = new LineNumberReader(new FileReader(holidaysFile))) {
            String line;
            while ((line = lnr.readLine()) != null) {
                if (line.indexOf('#') >= 0) {
                    // Remove comment sign and anything behind it.
                    line = line.substring(0, line.indexOf('#'));
                }
                if (line.matches("^[ \t]*$")) {
                    // A comment line: continue.
                    continue;
                }

                line = line.trim();
                if (line.contains("-")) {
                    // A range of dates.
                    String[] dates = line.split("-");
                    if (dates.length != 2) {
                        logger.warn("Invalid date range in line {}: {}", lnr.getLineNumber(), line);
                        continue;
                    }
                    LocalDate start;
                    LocalDate end;
                    try {
                        start = LocalDate.parse(dates[0].trim(), df);
                        end = LocalDate.parse(dates[1].trim(), df);
                    } catch (DateTimeParseException dtpe) {
                        logger.warn("Invalid date format in line {}: {}", lnr.getLineNumber(), line);
                        continue;
                    }
                    if (start.isAfter(end)) {
                        logger.warn("Start has to be before end in line {}: {}", lnr.getLineNumber(), line);
                        continue;
                    }
                    Duration duration = Duration.between(start.atStartOfDay(), end.atStartOfDay());
                    if (duration.toDays() > 100) {
                        logger.warn("More than 100 days between start and end in line {}: {}. "
                                + "Split into several entries.", lnr.getLineNumber(), line);
                        continue;
                    }
                    // Put every date into the set.
                    logger.debug("Adding vacation range: {} to {}", start, end);
                    LocalDate current = start;
                    while (!current.isAfter(end)) {
                        localVacations.add(current);
                        current = current.plusDays(1);
                    }
                } else {
                    // A single entry.
                    LocalDate date;
                    try {
                        date = LocalDate.parse(line, df);
                    } catch (DateTimeParseException dtpe) {
                        logger.warn("Invalid date format in line {}: {}", lnr.getLineNumber(), line);
                        continue;
                    }
                    logger.debug("Adding vacation: {}", date);
                    localVacations.add(date);
                }
            }

            logger.info("Parsed vacations: {} distinct days", localVacations.size());
            synchronized (this) {
                vacations = localVacations;
                lastRead = System.currentTimeMillis();
            }
        } catch (FileNotFoundException e) {
            // almost not possible because we check for existence
            logger.warn("Vacation file disappeared.");
        } catch (IOException ioe) {
            logger.error("IOException while reading fomr vacation file", ioe);
        }
    }
}
