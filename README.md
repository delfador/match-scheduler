![build](https://github.com/delfador/match-scheduler/actions/workflows/gradle.yml/badge.svg)

# Match scheduler

If you are faced with the problem of creating an _alternating, multi-round
schedule of matches for a group of players including one or more idle players
per round_, then the **match scheduler** might be just what you are looking for.

## The scheduling task

As an example, we'll consider the scheduling of tennis or padel
(doubles) matches, but the general idea could be applicable to similar
situations. Given a fixed number of players and a fixed number of rounds, the
scheduling task can be described as follows. In each round, all players have to
be partitioned in groups of four. These groups will play a match in that round.
If the number of players is not a multiple of four, then the remaining players
are idle (non-playing) that round. Note that we are **not** assigning either
fixed or non-fixed pairs to play a match. The exact matchup is determined by the
players themselves and is not part of the scheduling task.

For example, for six players and eight rounds, a schedule could be created by
rotating the players each round.

| Round | Match 1    | Idle |
|:-----:|------------|------|
|   1   | 1, 2, 3, 4 | 5, 6 |
|   2   | 6, 1, 2, 3 | 4, 5 |
|   3   | 5, 6, 1, 2 | 3, 4 |
|   4   | 4, 5, 6, 1 | 2, 3 |
|   5   | 3, 4, 5, 6 | 1, 2 |
|   6   | 2, 3, 4, 5 | 6, 1 |
|   7   | 1, 2, 3, 4 | 5, 6 |
|   8   | 6, 1, 2, 3 | 4, 5 |

This schedule has several undesirable properties.

- Player 1, 2, and 3 each play six matches, while player 5 plays only four.
- Every player has two consecutive idle rounds.
- Players 1 and 2 participate in the same match five times, while player 1 and 5
  play together twice.

It is possible to create a schedule that improves on these drawbacks. For six
players and eight rounds, it might be possible to do this manually, but as the
numbers increase this becomes a tedious task. The goal of this project is to
automate this scheduling process.

## Scheduling goals

The objective of this project is to create a scheduler that is generally
applicable: can be used for any number of players, number of rounds, and number
of players per match. Furthermore, the resulting schedule should meet the
following properties as best as possible.

1. At the end of all rounds, all players should have played the same number of
   rounds, or, if that's not possible, the number of played matches should
   differ at most by one.
2. The idle rounds of a player should be nicely spread over all rounds. In other
   words, the length of the playing streaks should all be close to each other.
3. The number of times each pair of players is participating in the same match
   should be close for all pairs.

Roughly speaking, most would agree that the properties are listed in the order
of importance. Of course, it is possible to add more desirable properties of a
schedule, and these might be ideas for further improvement.

## Limitations

As indicated earlier, currently, the scheduler does not specify the exact
matchups, which may be a requirement in some cases. Note however, that it is
possible to use the scheduler to create matchups for singles matches or fixed
doubles pairs: simply specify two players per match. However, you will lose the
possibility of varying doubles pairs in the latter case.

## Installation

The scheduler is a console application in build in
[Kotlin](https://kotlinlang.org/) and to be able to run the application you will
need **Java JDK version 21** for your system. If you don't have Java installed,
you can pick a version [here](https://whichjdk.com/).

### Source code

You can compile and run the application from the source code using
[Gradle](https://gradle.org/):

```bash
./gradlew run
```

or

```
.\gradlew.bat run
```

### Distribution version

Alternatively, you can download the distribution version, unzip the
[Package.zip](https://github.com/delfador/match-scheduler/releases/latest/download/Package.zip)
archive from
the [latest release](https://github.com/delfador/match-scheduler/releases/latest),
and run the executable for your system from the `bin` directory.

## Running and configuration

If you run the scheduler, you will be presented with the questions of how many
players and how many rounds should be targeted. After a few seconds, a schedule
will be presented alongside with some basic properties of the schedule. The
schedule will also be saved in CSV format, and the overview with the basic
properties in another file.

### Example

For instance, if you want to create a schedule for six players and twelve
rounds, you'll see something like this.

```
MATCH SCHEDULER
Number of players (10) > 6
Number of rounds (20) > 12
Solving...
Scores: 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0

PROBLEM
Number of players: 6
Number of rounds: 12
Payers per match: 4
Playing players per round: 4
Idle players per round: 2
Ideal playing streak: 2.00 rounds
Acceptable playing streak: 1..3 (excluding start/end boundaries)
Average pair frequency after 12 rounds: 4.80

SCHEDULE
 1: [3, 1, 5, 2], [4, 6]
 2: [4, 2, 3, 6], [5, 1]
 3: [4, 1, 3, 5], [6, 2]
 4: [1, 4, 6, 2], [3, 5]
 5: [6, 3, 5, 2], [4, 1]
 6: [5, 2, 1, 4], [3, 6]
 7: [4, 6, 5, 3], [1, 2]
 8: [1, 3, 4, 2], [5, 6]
 9: [6, 1, 3, 5], [4, 2]
10: [5, 2, 4, 6], [3, 1]
11: [1, 6, 3, 2], [5, 4]
12: [4, 5, 6, 1], [2, 3]

MATCHES PLAYED
 1: {1=1, 2=1, 3=1, 5=1}
 2: {1=1, 2=2, 3=2, 4=1, 5=1, 6=1}
 3: {1=2, 2=2, 3=3, 4=2, 5=2, 6=1}
 4: {1=3, 2=3, 3=3, 4=3, 5=2, 6=2}
 5: {1=3, 2=4, 3=4, 4=3, 5=3, 6=3}
 6: {1=4, 2=5, 3=4, 4=4, 5=4, 6=3}
 7: {1=4, 2=5, 3=5, 4=5, 5=5, 6=4}
 8: {1=5, 2=6, 3=6, 4=6, 5=5, 6=4}
 9: {1=6, 2=6, 3=7, 4=6, 5=6, 6=5}
10: {1=6, 2=7, 3=7, 4=7, 5=7, 6=6}
11: {1=7, 2=8, 3=8, 4=7, 5=7, 6=7}
12: {1=8, 2=8, 3=8, 4=8, 5=8, 6=8}

PLAYING STREAKS (excluding start/end boundaries)
 1: [2, 1, 2] (min: 1, max: 2)
 2: [3, 1, 2] (min: 1, max: 3)
 3: [1, 3, 1] (min: 1, max: 3)
 4: [3, 3, 1] (min: 1, max: 3)
 5: [1, 3, 2] (min: 1, max: 3)
 6: [1, 2, 1] (min: 1, max: 2)

PAIR FREQUENCY
4: [(1, 6), (2, 5), (3, 4)]
5: [(1, 2), (1, 3), (1, 5), (1, 4), (2, 3), (2, 4), (2, 6), (3, 5), (3, 6), (4, 6), (4, 5), (5, 6)]

MATCH FREQUENCY
1: 12 matches


WEIGHTED SUM
  Total matches played in 8..9       : weight =   6.00, score =   0.0000, weighted score =   0.0000
  Streaks in 1..3                    : weight =   5.00, score =   0.0000, weighted score =   0.0000
  Final pair frequency in 4..5       : weight =   1.00, score =   0.0000, weighted score =   0.0000

  TOTAL SCORE: 0.0000
```

### Configuration

The application has several other options that can be set via a JSON
configuration file only. The file should be named `options.json` and be
accessible from the _current working directory_. A sample options file is shown
below. It is not necessary to include all options. If options are left out,
sensible defaults will be selected.

```json
{
  "numberOfPlayers": 10,
  "numberOfRounds": 20,
  "playersPerMatch": 4,
  "scoringWeights": {
    "totalMatchesPlayedWeight": 6.0,
    "playingStreakWeight": 5.0,
    "pairFrequencyWeight": 1.0
  },
  "moveWeights": {
    "swapPlayerWeight": 10.0,
    "rotatePlayersWeight": 0.0,
    "swapRoundWeight": 4.0
  },
  "maxIter": 100000,
  "parallelSolvers": 16,
  "scheduleCsv": "schedule.csv",
  "scheduleDetails": "schedule-details.txt"
}
```

`numberOfPlayers`
: Default value for the number of players, which can be overridden when running
the app.

`numberOfRounds`
: Default value for the number of rounds, which can be overridden when running
the app.

`playersPerMatch`
: The number of players per match.

`scoringWeights`
: Section for specifying the weights of the goal function. Use with care:
specifying a large weight for a criterion you find important, does not
necessarily lead to better solutions.

- `totalMatchesPlayedWeight`
- `playingStreakWeight`
- `pairFrequencyWeight`

`moveWeights`
: Section for specifying the weights for searching the _local neighborhood_. See
the [Algorithm](#algorithm) section for more details.

`maxIter`
: The (maximum) number of iterations of the algorithm.

`parallelSolvers`
: The scheduler will run multiple solvers all trying to solve the same
scheduling problem in parallel. Defaults to the number of cores of your system.
You can also specify a larger number, but you'll have to wait a bit longer
before the schedule is ready.

`scheduleCsv`
: The filename where the schedule in CSV format will be saved.

`scheduleDetails`
: The filename where the schedule details will be saved.

## Algorithm

The following might be a bit technical, but if you're interested in mathematics
and optimization, please read on!

The match scheduler optimizes the schedule using
a [simulated annealing](https://en.wikipedia.org/wiki/Simulated_annealing)
algorithm. This is a _global optimization_ technique that explores the search
space by randomly moving to neighboring states (neighbor schedules in our case)
combined with a probabilistic acceptance criterion that should prevent the
algorithm from getting stuck in a local optimum.

The objective we are trying to optimize is focussed on the three
[scheduling goals](#scheduling-goals). Based on the problem's input data
(number of players, number of rounds, and player per match), the scheduler
determines a reasonable target for each goal. Any deviation from these targets
will be penalized in the objective function.

The algorithm can move from one schedule to a neighbor schedule by randomly
choosing one of the following methods:

- **Swap players:** randomly swap two players that are not in the same match for
  a randomly chosen round.
- **Rotate players:** rotate the sequence of players for a randomly chosen
  round.
- **Swap rounds:** swap two randomly chosen rounds, keeping the matches in those
  rounds fixed.

The probabilistic acceptance criterion is guided by the algorithm's
_temperature_. Tuning the temperature's cooling schedule and the problem's
objective function is a crucial, but not straightforward endeavor. The match
scheduler automatically tunes the cooling schedule, based upon the number of
iteration and scoring weights. Even with the tuned settings, the algorithm still
ends up at a local optimum sometimes, which is also why running the algorithm in
parallel or multiple times might yield a better solution.

## Examples

In the [examples](./examples) folder, you can find a collection of schedules
obtained with the match scheduler.

- 5 players, 15
  rounds: [schedule](examples%2Fschedule-5-15.csv), [details](examples%2Fschedule-details-5-15.txt)
- 6 players, 12
  rounds: [schedule](examples%2Fschedule-6-12.csv), [details](examples%2Fschedule-details-6-12.txt)
- 6 players, 24
  rounds: [schedule](examples%2Fschedule-6-24.csv), [details](examples%2Fschedule-details-6-24.txt)
- 8 players, 16
  rounds: [schedule](examples%2Fschedule-8-16.csv), [details](examples%2Fschedule-details-8-16.txt)
- 9 players, 25
  rounds: [schedule](examples%2Fschedule-9-25.csv), [details](examples%2Fschedule-details-9-25.txt)
- 10 players, 20
  rounds: [schedule](examples%2Fschedule-10-20.csv), [details](examples%2Fschedule-details-10-20.txt)
- 10 players, 30
  rounds: [schedule](examples%2Fschedule-10-30.csv), [details](examples%2Fschedule-details-10-30.txt)
- 7 players, 12 rounds: [schedule](examples%2Fschedule-7-12-2.csv), [details](examples%2Fschedule-details-7-12-2.txt) (2 players per match)

## Contributing & feedback

If you like the **match scheduler**, have comments, want to contribute, have
feature requests, please let me know.
