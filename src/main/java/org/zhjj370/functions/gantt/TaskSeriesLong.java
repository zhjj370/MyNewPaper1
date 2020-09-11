/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2016, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.]
 *
 * ---------------
 * TaskSeries.java
 * ---------------
 * (C) Copyright 2002-2016, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 06-Jun-2002 : Version 1 (DG);
 * 07-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 24-Oct-2002 : Added methods to get TimeAllocation by task index (DG);
 * 10-Jan-2003 : Renamed GanttSeries --> TaskSeries (DG);
 * 30-Jul-2004 : Added equals() method (DG);
 * 09-May-2008 : Fixed cloning bug (DG);
 * 03-Jul-2013 : Use ParamChecks (DG);
 * 19-Jan-2019 : Added missing hashCode (TH);
 *
 */

package org.zhjj370.functions.gantt;

import org.jfree.chart.util.Args;
import org.jfree.chart.util.CloneUtils;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.general.Series;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A series that contains zero, one or many {@link Task} objects.
 * <P>
 * This class is used as a building block for the {@link TaskSeriesCollection}
 * class that can be used to construct basic Gantt charts.
 */
public class TaskSeriesLong<K extends Comparable<K>> extends Series{

    /** Storage for the tasks in the series. */
    private List<TaskLong> tasks;

    /**
     * Constructs a new series with the specified name.
     *
     * @param name  the series name ({@code null} not permitted).
     */
    public TaskSeriesLong(K name) {
        super(name);
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the series and sends a
     * {@link org.jfree.data.general.SeriesChangeEvent} to all registered
     * listeners.
     *
     * @param task  the task ({@code null} not permitted).
     */
    public void add(TaskLong task) {
        Args.nullNotPermitted(task, "task");
        this.tasks.add(task);
        fireSeriesChanged();
    }

    /**
     * Removes a task from the series and sends
     * a {@link org.jfree.data.general.SeriesChangeEvent}
     * to all registered listeners.
     *
     * @param task  the task.
     */
    public void remove(TaskLong task) {
        this.tasks.remove(task);
        fireSeriesChanged();
    }

    /**
     * Removes all tasks from the series and sends
     * a {@link org.jfree.data.general.SeriesChangeEvent}
     * to all registered listeners.
     */
    public void removeAll() {
        this.tasks.clear();
        fireSeriesChanged();
    }

    /**
     * Returns the number of items in the series.
     *
     * @return The item count.
     */
    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    /**
     * Returns a task from the series.
     *
     * @param index  the task index (zero-based).
     *
     * @return The task.
     */
    public TaskLong get(int index) {
        return this.tasks.get(index);
    }

    /**
     * Returns the task in the series that has the specified description.
     *
     * @param description  the name ({@code null} not permitted).
     *
     * @return The task (possibly {@code null}).
     */
    //todo
    public TaskLong get(String description) {
        TaskLong result = null;
        int count = this.tasks.size();
        for (int i = 0; i < count; i++) {
            TaskLong t = this.tasks.get(i);
            if (t.getDescription().equals(description)) {
                result = t;
                break;
            }
        }
        return result;
    }

    /**
     * Returns the tasks in the series that has the specified description.
     *
     * @param description  the name ({@code null} not permitted).
     *
     * @return The task (possibly {@code null}).
     */
    public List<TaskLong> getTaskList(String description) {
        List<TaskLong> result = new ArrayList<>();
        int count = this.tasks.size();
        for (int i = 0; i < count; i++) {
            TaskLong t = this.tasks.get(i);
            if (t.getDescription().equals(description)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Returns an unmodifialble list of the tasks in the series.
     *
     * @return The tasks.
     */
    public List<TaskLong> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }

    /**
     * Tests this object for equality with an arbitrary object.
     *
     * @param obj  the object to test against ({@code null} permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TaskSeriesLong)) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        TaskSeriesLong that = (TaskSeriesLong) obj;
        if (!this.tasks.equals(that.tasks)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode(){
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.tasks);
        return hash;
    }

    /**
     * Returns an independent copy of this series.
     *
     * @return A clone of the series.
     *
     * @throws CloneNotSupportedException if there is some problem cloning
     *     the dataset.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        TaskSeriesLong clone = (TaskSeriesLong) super.clone();
        clone.tasks = CloneUtils.cloneList(this.tasks);
        return clone;
    }

}
