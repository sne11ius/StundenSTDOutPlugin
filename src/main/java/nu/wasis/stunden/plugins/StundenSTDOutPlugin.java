package nu.wasis.stunden.plugins;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import nu.wasis.stunden.model.Day;
import nu.wasis.stunden.model.Entry;
import nu.wasis.stunden.model.WorkPeriod;
import nu.wasis.stunden.plugin.OutputPlugin;
import nu.wasis.stunden.util.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

@PluginImplementation
public class StundenSTDOutPlugin implements OutputPlugin {

    // private static final Logger LOG = Logger.getLogger(StundenSTDOutPlugin.class);

    private static final void p(final String message) {
        System.out.println(message);
    }

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
//            final Map<String, Duration> durations = new HashMap<>();
            for (final Entry entry : day.getEntries()) {
                p(entry.getBegin().toString(DateUtils.TIME_FORMATTER) + " - " + entry.getEnd().toString(DateUtils.TIME_FORMATTER) + ": " + entry.getProject().getName() + (entry.isBreak() ? " (break)" : ""));
                if (!entry.isBreak()) {
                	totalWorkDuration = totalWorkDuration.plus(entry.getDuration());
//	                final Duration newDuration = entry.getDuration();
//	                if (!durations.containsKey(entry.getProject().getName())) {
//	                    durations.put(entry.getProject().getName(), newDuration);
//	                } else {
//	                    final Duration originalPeriod = durations.get(entry.getProject().getName());
//	                    durations.put(entry.getProject().getName(), originalPeriod.plus(newDuration));
//	                }
                }
            }
            p("Summary:");
//            for (final Map.Entry<String, Duration> entry : durations.entrySet()) {
//                p("\t" + entry.getKey() + ": " + entry.getValue().toPeriod().toString(DateUtils.PERIOD_FORMATTER));
//            }
            p("\tTotal work time: " + DateUtils.PERIOD_FORMATTER.print(day.getWorkDuration().toPeriod()));
        }
        p("TOTAL");
        p("=====");
        p("Work duration:\t" + DateUtils.PERIOD_FORMATTER.print(totalWorkDuration.toPeriod()));
        final long minutesPerDay = totalWorkDuration.getStandardMinutes() / workPeriod.getDays().size();
        p("Days:\t\t" + workPeriod.getDays().size());
		p("Work/day:\t" + DateUtils.TIME_FORMATTER.print(new DateTime(0, 1, 1, (int)minutesPerDay / 60, (int)minutesPerDay % 60)));
    }

    @Override
    public Class<?> getConfigurationClass() {
        return null;
    }
}
