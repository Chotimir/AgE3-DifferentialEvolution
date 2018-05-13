"""
This script evaluates the *.log files produced by the solver of the Differential Evolution problem for any of the
algorithms.

Note that this script evaluates all files placed in the {LOGS_DIR} directory, where the {LOGS_DIR} represents the path
to a directory containing *.log files produced by any of AgE3-DifferentialEvolution algorithms, as well as copies of
configuration files used for customization of this algorithm. The {LOGS_DIR} path is provided by the first script
parameter.
"""

# TODO: Implement evaluation of log entries for a particular workplace in a tick (i.e. the log entry starting with the
# '[W]' prefix) and a log entry with the best solution (i.e. the log entry staring with the '[B]' prefix).

import os
import sys

import numpy as np
from matplotlib import pyplot as plt


def main():
    # PARSING THE CONFIGURATION FILE.
    config = dict()

    with open('C:/Repos/age3-ogr-labs-correct/age3-ogr-labs/AgE3-DifferentialEvolution/Commons/src/main/resources/pl/edu/agh/age/de/common/common-config.properties') as properties_file:
        for entry in properties_file:
            if len(entry.strip()) > 0 and (not entry.startswith('#')):
                key, value = entry.split('=', 1)
                config[key.strip()] = value.strip()

    logging_interval_seconds = float(config['de.logging.interval-in-milliseconds']) / 1000
    simulation_time = int(config['de.stop-condition.time-in-seconds'])
    ticks_number = int(simulation_time / logging_interval_seconds)


    # PARSING *.LOG FILES.
    logs_directory_path = sys.argv[1]
    best_solutions = list()
    evaluations_count = list()

    for log_file_path in os.listdir(logs_directory_path):
        if not log_file_path.endswith('.log'):
            continue

        with open(os.path.join(logs_directory_path, log_file_path)) as log_file:
            best_solutions_by_file = list()
            evaluations_count_by_file = list()

            for entry in log_file:
                if entry.startswith('[S]'):
                    splitted_entry = entry.split(';', 3)
                    best_solutions_by_file.append(float(splitted_entry[2]))
                    evaluations_count_by_file.append(int(splitted_entry[3]))

            assert len(best_solutions_by_file) == ticks_number
            assert len(evaluations_count_by_file) == ticks_number
            best_solutions.append(best_solutions_by_file)
            evaluations_count.append(evaluations_count_by_file)

    # PROCESSING COLLECTED DATA.
    best_solutions_by_tick = np.array(
        [np.array([solution[tick] for solution in best_solutions]) for tick in range(ticks_number)])
    mean_evaluations_count_by_tick = np.array(
        [np.array([evaluations[tick] for evaluations in evaluations_count]).mean() for tick in range(ticks_number)])
    start, end, step = 0, ticks_number, 1

    # GENERAL PLOT SETTINGS.
    fig, solutions_plot = plt.subplots(figsize=(14, 7))
    plt.xticks(fontsize=8, rotation=90)
    plt.xlabel('Simulation time [s]', fontsize=10)

    # SOLUTIONS PLOT.
    solutions_plot.boxplot(best_solutions_by_tick[start:end:step].T, notch=False, showmeans=True, meanline=True,
                           flierprops=dict(markerfacecolor='white', markeredgecolor='black', markersize=7, marker='.'),
                           meanprops=dict(color='green', linestyle='-'),
                           whiskerprops=dict(color='purple', linestyle='-'),
                           capprops=dict(color='purple'))

    plt.yscale('log')
    plt.yticks(fontsize=10)
    plt.ylabel('Best fitness value', fontsize=10)

    # EVALUATIONS PLOT.
    evaluations_plot = solutions_plot.twinx()
    evaluations_plot.plot(np.arange(1, (ticks_number - start)/step + 1), mean_evaluations_count_by_tick[start:end:step], 'y-')

    y_tick_size = 1e5
    y_ticks_range = np.arange(plt.gca().get_ylim()[0], plt.gca().get_ylim()[1] + y_tick_size, y_tick_size)
    plt.yticks(y_ticks_range, ['%.1E' % tick if tick != 0 else '0.0' for tick in y_ticks_range], fontsize=10) # %.1E
    plt.ylabel('Evaluations count', fontsize=10)

    # OX TICKS PROPERTIES.
    plt.xlim((0, np.ceil((ticks_number - start)/step) + 1))
    x_ticks_range = np.arange(logging_interval_seconds, simulation_time + logging_interval_seconds, logging_interval_seconds)[start:end:step]
    plt.xticks(np.arange(1, (ticks_number - start)/step + 1), ['%.1f' % tick for tick in x_ticks_range])

    # GENERAL PLOT SETTINGS.
    plt.title('Best fitness and evaluations count in the time domain')
    plt.subplots_adjust(left=0.07, bottom=0.10, right=0.93, top=0.96, wspace=0.0, hspace=0.0)
    plt.show()


if __name__ == '__main__':
    main()
