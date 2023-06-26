package org.example;

import java.util.ArrayList;
import java.util.List;

public class Winery {
    private static Winery INSTANCE;

    private static List<Wine> wineList = new ArrayList<>();

    private Winery() {
    }

    public static Winery getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Winery();
        }

        return INSTANCE;
    }

    // getters and setters
    void add(Wine wine){
        wineList.add(wine);
    }

    void remove(Wine wine){
        wineList.remove(wine);
    }

    List<Wine> getList(){
        return wineList;
    }

    public static List<Wine> getWineOfType(String type) {
        List<Wine> newWineList = new ArrayList<>();
        for(Wine wine : wineList){
            if(wine.getType().equals(type)){
                newWineList.add(wine);
            }
        }
        return newWineList;
    }
    public static List<Wine> getSortedByName() {
        List<Wine> newWineList = new ArrayList<>(wineList);
        newWineList.sort((o1, o2) -> {
            return o1.getName().compareTo(o2.getName());
        });
        return newWineList;
    }

    public static List<Wine> getSortedByPrice() {
        List<Wine> newWineList = new ArrayList<>(wineList);
        newWineList.sort((o1, o2) -> {
            if (o1.getPrice()>o2.getPrice())
                return 1;
            if (o1.getPrice()<o2.getPrice())
                return -1;
            return 0;
        });
        return newWineList;
    }


    public static void buildList(){
        wineList.add(new Wine(1,"Vino bianco", 23.34, "White"));
        wineList.add(new Wine(3,"Vino rose", 34.65, "Rose"));
        wineList.add(new Wine(100,"Vino rosso", 12.65, "Red"));
        wineList.add(new Wine(7,"Altro vino", 49, "White"));
    }
}
