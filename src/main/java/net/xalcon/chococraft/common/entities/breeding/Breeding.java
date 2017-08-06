package net.xalcon.chococraft.common.entities.breeding;


import net.xalcon.chococraft.common.entities.ChocoboColor;
import net.xalcon.chococraft.common.entities.EntityChocobo;

public class Breeding {

    public static ChocoboColor getColour(EntityChocobo firstParent, EntityChocobo secondParent) {
        return firstParent.getChocoboColor();

        /*int randColour = firstParent.getEntityWorld().rand.nextInt(100);
        boolean bothParentsFedGold = firstParent.fedGoldenGyshal && secondParent.fedGoldenGyshal;
        HashMap<String, List<HashMap<String, String>>> secondParentColourMaps = ChocoCraft2.instance.getConfig().getBreedingInfoHashmap().get(firstParent.getChocoboColor().name());
        for(Map.Entry<String, List<HashMap<String, String>>> secondParentColourEntry : secondParentColourMaps.entrySet()) {
            if(secondParentColourEntry.getKey().contains(secondParent.getChocoboColor().name())) {
                List<HashMap<String, String>> breedingInfoList = secondParentColourEntry.getValue();
                boolean flag = false;
                for(HashMap<String, String> breedingInfo : breedingInfoList) {
                    String childColour = breedingInfo.get("childColour");
                    String conditions = breedingInfo.get("conditions");
                    String random = breedingInfo.get("random");
                    if(!conditions.equals("none")) {
                        if(conditions.equals("bothParentsFedGold")) {
                            flag = true;
                            if(!bothParentsFedGold) {
                                continue;
                            }
                        }
                    } else {
                        if(flag && bothParentsFedGold) {
                            continue;
                        }
                    }
                    if(!random.equals("none")) {
                        String[] parts = random.split(Pattern.quote(" "));
                        if(parts[0].equals("above")) {
                            if(!(randColour > Integer.parseInt(parts[1])))
                                continue;
                        } else if(parts[0].equals("under")) {
                            if(!(randColour < Integer.parseInt(parts[1])))
                                continue;
                        }
                    }
                    if(childColour.equals("secondParent")) {
                        return secondParent.getChocoboColor();
                    } else {
                        return EntityChocobo.ChocoboColor.valueOf(childColour);
                    }

                }
            }
        }
        return YELLOW;*/
    }

}
