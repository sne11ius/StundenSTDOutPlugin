package nu.wasis.stunden.plugins;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.OutputPlugin;
import nu.wasis.stunden.util.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

/**
 * This {@link OutputPlugin} prints the {@link WorkPeriod} to stdout. Result
 * will look something like this:
 * <pre>
 * Start of period	: 2013-09-02
 * End of period	: 2013-09-30
 * ============================
 * 
 * 1999-09-09
 * ==========
 * 09:00 - 10:00: Lunch (break) ==> 01:00
 * 10:00 - 10:30: Project1 ==> 00:30
 * 10:30 - 17:45: Project2 ==> 07:15
 * 17:45 - 18:15: Project3 ==> 00:30
 * Summary:
 *     Total work time: 08:15
 * 
 * [...]
 * 
 * TOTAL
 * =====
 * Work duration: 167:00
 * Days:          21
 * Work/day:      07:57
 * 
 * </pre>
 */
@PluginImplementation
public class StundenSTDOutPlugin implements OutputPlugin {

    // private static final Logger LOG = Logger.getLogger(StundenSTDOutPlugin.class);

	/**
	 * Shortcut for System.out.println ftw!
	 * @param message The message to stdout.
	 */
    private static final void p(final String message) {
        System.out.println(message);
    }

    /**
     * Print the given {@link WorkPeriod} to stdout. This plugin cannot be
     * configured, so the <code>config</code> param is ignored.
     * 
     * @param config Ignored.
     */
    @Override
    public void output(final WorkPeriod workPeriod, final Object config) {
        p("Start of period\t: " + workPeriod.getBegin().toString(DateUtils.DATE_FORMATTER));
        p("End of period\t: " + workPeriod.getEnd().toString(DateUtils.DATE_FORMATTER));
        p("============================");

        if (workPeriod.getDays().isEmpty()) {
            p("[Period contains no entries.]");
            return;
        }
        
        Duration totalWorkDuration = new Duration(0);

        for (final Day day : workPeriod.getDays()) {
            p("");
            p(day.getDate().toString(DateUtils.DATE_FORMATTER));
            p("==========");
            for (final Entry entry : day.getEntries()) {
                String line = entry.getBegin().toString(DateUtils.TIME_FORMATTER) + " - " + entry.getEnd().toString(DateUtils.TIME_FORMATTER) + ": " + entry.getProject().getName() + (entry.isBreak() ? " (break)" : "");
                // line = StringUtils.rightPad(line, 60);
                // line = line + DateUtils.PERIOD_FORMATTER.print(entry.getDuration().toPeriod());
                line = line + " ==> " + DateUtils.PERIOD_FORMATTER.print(entry.getDuration().toPeriod());
				p(line);
                if (!entry.isBreak()) {
                	totalWorkDuration = totalWorkDuration.plus(entry.getDuration());
                }
            }
            p("Summary:");
            p("\tTotal work time: " + DateUtils.PERIOD_FORMATTER.print(day.getWorkDuration().toPeriod()));
        }
        p("");
        p("TOTAL");
        p("=====");
        p("Work duration:\t" + DateUtils.PERIOD_FORMATTER.print(totalWorkDuration.toPeriod()));
        final long minutesPerDay = totalWorkDuration.getStandardMinutes() / workPeriod.getDays().size();
        p("Days:\t\t" + workPeriod.getDays().size());
		p("Work/day:\t" + DateUtils.TIME_FORMATTER.print(new DateTime(0, 1, 1, (int)minutesPerDay / 60, (int)minutesPerDay % 60)));
    }

    /**
     * This plugin cannot be configured.
     */
    @Override
    public Class<?> getConfigurationClass() {
        return null;
    }
}
