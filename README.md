# performance-testing
The code for my personal performance testing

![alt tag](https://raw.githubusercontent.com/trieu/performance-testing/master/benchmark-rfx-nodejs.png)

## requirements
* sudo apt-get install apache2-utils
* Using Apache Bench: http://www.petefreitag.com/item/689.cfm

* sudo apt-get install gnuplot-qt rlwrap
* Using GnuPlot http://www.bradlanders.com/2013/04/15/apache-bench-and-gnuplot-youre-probably-doing-it-wrong

## Commands
* ab -n 50000 -c 500 -k -g nodejs-data.tsv http://localhost:3001/ping
* ab -n 50000 -c 500 -k -g rfx-data.tsv http://localhost:8080/ping
* gnuplot benchmark-rfx-nodejs.tpl

Happy performance hacking ! 