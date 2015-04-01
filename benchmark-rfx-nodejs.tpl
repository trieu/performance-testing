# output as png image
set terminal png size 700,500
 
# save file
set output "benchmark-rfx-nodejs.png"
 
# graph title
set title "Benchmarking HTTP server: NodeJS vs RFX (run by @tantrieuf31)"

set font "Bold-Times-Roman,20"
 
#nicer aspect ratio for image size
set size 1,0.7
 
# enable grid on y-axis
set grid y
 
# x-axis label
set xlabel "Request"
 
# y-axis label
set ylabel "Response Time (ms)"

set ytics (0,5,10,20,40,60,80,100,120,140,160)
 
# plot data from tsv data
plot "nodejs-data.tsv" using 10 smooth sbezier with lines title "NodeJS with Express:", \
"rfx-data.tsv" using 10 smooth sbezier with lines title "RFX with Java8:"
