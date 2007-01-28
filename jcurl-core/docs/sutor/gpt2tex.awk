
# Line starting with other than '\' or '%':
$0 ~ /^[\t ]*[^\\%]/ {
    printf("%% gpt2tex.awk removed: ");
}

# The "\gnuplot" font-command:
$0 == "\\gnuplot" {
    printf("%% gpt2tex.awk removed: ");
}

{
    print
}
