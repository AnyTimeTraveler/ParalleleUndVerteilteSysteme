for i in $(seq 1 4); do
  echo "Testing v${i} for equivalence:"
  /usr/bin/mcrl22lps TrafficLights_v${i}.mcrl2 TrafficLights_v${i}_own.lps --quiet
  /usr/bin/lps2lts TrafficLights_v${i}_own.lps TrafficLights_v${i}_own.lts --quiet
  /usr/bin/ltsconvert TrafficLights_v${i}_own.lts TrafficLights_v${i}_own_bb.lts --equivalence=branching-bisim --quiet
  /usr/bin/ltscompare TrafficLights_v${i}_own_bb.lts TrafficLights_v${i}.lts --counter-example --equivalence=branching-bisim
  echo
done
