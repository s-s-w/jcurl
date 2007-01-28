#!/bin/sh
gnuplot sutor.gpt | awk -f gpt2tex.awk > sutor.ltx

latex sutor
latex sutor
