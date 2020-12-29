# Bi-level Experiment (bilevel-experiment)

## Introduction

This project contains the source code for a distributed software application used for human subject experiments between 2017-2020 by the Collective Design Lab at Stevens Institute of Technology to study a bi-level model of design. The experiment runs sessions with two pairs of designers (four designers total) plus a manager (research administrator). Lower-level decisions assign one design parameter per designer with nine alternatives (indices 0-9). Upper level decisions assign one strategy per designer with two alternatives (index 0 or 1).

## Getting Started

This project includes a Maven `pom.xml` file which simplifies dependency management. If you are using an integrated development environment (IDE) like Eclipse, import it as an existing Maven project to load all dependencies.

The application can be launched from the `Main.java` file; however, it requires some command line arguments (in Eclipse: Run > Run Configurations... > Arguments) to configure:
 * To launch one or more designer applications, include the `-d` argument and a space-delimited list of zero-based designer indices (e.g., `-d 0 2` will launch designers 0 and 2).
 * To launch the manager application, include the `-m` argument and the path to an experiment JSON file such as the provided one used in our experiments (e.g., `-m experiment.json`).
 * Any application requires the `-h` argument to specify the manager hostname or IP address. If all applications are running on one machine, you can use localhost (e.g., `-h localhost`).

## Default Configuration

The default `experiment.json` file contains four training tasks and twelve experimental tasks, following published results.

## Acknowledgement

This material is based upon work supported by the National Science Foundation under Grant No. 1742971. Any opinions, findings, and conclusions or recommendations expressed in this material are those of the author(s) and do not necessarily reflect the views of the National Science Foundation.