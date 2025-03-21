/*
 * The MIT License
 *
 * Copyright (c) 2018 Sven Schoenung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.jenkins.plugins.view.calendar.time;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.TimeZone;

import static io.jenkins.plugins.view.calendar.test.CalendarUtil.hours;
import static io.jenkins.plugins.view.calendar.test.CalendarUtil.mom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MomentRangeTest {

    private static TimeZone defaultTimeZone;

    @BeforeAll
    static void beforeClass() {
        MomentRangeTest.defaultTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
    }

    @AfterAll
    static void afterClass() {
        TimeZone.setDefault(MomentRangeTest.defaultTimeZone);
    }

    @Test
    void testConstructorInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> new MomentRange(mom("2018-01-02 00:00:00 UTC"), mom("2018-01-01 23:59:59 UTC")));
    }

    @Test
    void testConstructorInvalidRangeSameMoment() {
        assertThrows(IllegalArgumentException.class, () -> new MomentRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-01 00:00:00 UTC")));
    }

    @Test
    void testConstructorValidRange() throws ParseException {
        new MomentRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-02 00:00:00 UTC"));
    }

    @Test
    void testIsValidRange() throws ParseException {
        assertThat(MomentRange.isValidRange(mom("2018-01-02 00:00:00 UTC"), mom("2018-01-01 23:59:59 UTC")), is(false));
        assertThat(MomentRange.isValidRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-01 00:00:00 UTC")), is(false));
        assertThat(MomentRange.isValidRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-02 00:00:00 UTC")), is(true));
    }

    @Test
    void testToString() throws ParseException {
        String string = new MomentRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-02 00:00:00 UTC")).toString();
        assertThat(string, is("2018-01-01T01:00:00 - 2018-01-02T01:00:00"));
    }

    @Test
    void testDuration() throws ParseException {
        long duration = new MomentRange(mom("2018-01-01 00:00:00 UTC"), mom("2018-01-02 00:00:00 UTC")).duration();
        assertThat(duration, is(hours(24)));
    }
}
