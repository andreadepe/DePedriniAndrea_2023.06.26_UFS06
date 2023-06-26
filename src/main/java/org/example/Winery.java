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


    /*public Car getMostExpensive() {
        List<Car> carList = new ArrayList<>(vineList);
        carList.sort((o1, o2) -> {
            if (o1.getPrice()<o2.getPrice())
                return 1;
            if (o1.getPrice()>o2.getPrice())
                return -1;
            return 0;
        });
        return carList.get(0);
    }

    public List<Car> getSortedList() {
        List<Car> carList = new ArrayList<>(cars);
        carList.sort((o1, o2) -> {
            return o1.getBrand().compareTo(o2.getBrand());
        });
        return carList;
    }*/
}
