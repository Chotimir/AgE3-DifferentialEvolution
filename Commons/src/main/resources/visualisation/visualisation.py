"""
This script evaluates the *.log files produced by the solver of the Differential Evolution problem for any of the
algorithms.

Note that this script evaluates all files placed in the {BASE_DIR}/logs directory, where the {BASE_DIR} represents the
path to the main directory of any AgE3-DifferentialEvolution subproject (i.e. ClassicalEvolution, DifferentialEvolution
of HybridEvolution). The {BASE_DIR} path is provided by the first script parameter.
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

    with open(sys.argv[1] + '/../Commons/src/main/resources/pl/edu/agh/age/de/common/common-config.properties') as properties_file:
        for entry in properties_file:
            if len(entry.strip()) > 0 and (not entry.startswith('#')):
                key, value = entry.split('=', 1)
                config[key.strip()] = value.strip()

    logging_interval_seconds = int(config['de.logging.interval-in-milliseconds']) / 1000
    simulation_time = int(config['de.stop-condition.time-in-seconds'])
    ticks_number = int(simulation_time / logging_interval_seconds)

    # PARSING *.LOG FILES.
    logs_directory_path = sys.argv[1] + '/logs'
    best_solutions = list()
    evaluations_count = list()

    for log_file_path in os.listdir(logs_directory_path):
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
    assert len(best_solutions) == len(os.listdir(logs_directory_path))
    assert len(evaluations_count) == len(os.listdir(logs_directory_path))

    best_solutions_by_tick = np.array(
        [np.array([solution[tick] for solution in best_solutions]) for tick in range(ticks_number)])
    mean_evaluations_count_by_tick = np.array(
        [np.array([evaluations[tick] for evaluations in evaluations_count]).mean() for tick in range(ticks_number)])

    # SOLUTIONS PLOT.
    fig, solutions_plot = plt.subplots()
    solutions_plot.boxplot(best_solutions_by_tick.T, notch=False, showmeans=True, meanline=True,
                           flierprops=dict(markerfacecolor='white', markeredgecolor='black', markersize=7, marker='.'),
                           meanprops=dict(color='green', linestyle='-'),
                           whiskerprops=dict(color='purple', linestyle='-'),
                           capprops=dict(color='purple'))

    y_ticks_range = np.arange(plt.gca().get_ylim()[0], plt.gca().get_ylim()[1], 10)
    solutions_plot.set_yticks(y_ticks_range)

    solutions_plot.set_yticklabels([int(tick) for tick in y_ticks_range], fontdict=dict(fontsize=10))
    solutions_plot.set_ylabel('Best fitness value', fontsize=10)

    # OX AXIS PROPERTIES.
    x_ticks_range = np.arange(0, simulation_time + logging_interval_seconds, logging_interval_seconds)
    solutions_plot.set_xticks(np.arange(0, len(x_ticks_range) + 1))

    solutions_plot.set_xticklabels(['%.1f' % tick for tick in x_ticks_range], fontdict=dict(fontsize=10, rotation=90))
    solutions_plot.set_xlabel('Simulation time [s]', fontsize=10)

    # EVALUATIONS PLOT.
    evaluations_plot = solutions_plot.twinx()
    evaluations_plot.plot(mean_evaluations_count_by_tick, 'y-', zorder=1)

    y_ticks_range = np.arange(plt.gca().get_ylim()[0], plt.gca().get_ylim()[1], 1e5)
    evaluations_plot.set_yticks(y_ticks_range)

    evaluations_plot.set_yticklabels(['0.0'] + ['%.1E' % tick for tick in y_ticks_range[1:]], fontdict=dict(fontsize=10))
    evaluations_plot.set_ylabel('Evaluations count', fontsize=10)

    # GENERAL PLOT SETTINGS.
    plt.title('Global best fitness value and evaluations count in the time domain')
    plt.subplots_adjust(left=0.04, bottom=0.08, right=0.94, top=0.96, wspace=0.0, hspace=0.0)
    plt.show()


if __name__ == '__main__':
    main()
