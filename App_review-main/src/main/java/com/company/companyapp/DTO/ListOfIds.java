package com.company.companyapp.DTO;

import java.util.List;

public class ListOfIds {
    private List<String> listOfIds;
    public ListOfIds(){}
    public ListOfIds(List<String> listOfIds) {
        this.listOfIds = listOfIds;
    }

    public List<String> getListOfIds() {
        return listOfIds;
    }

    public void setListOfIds(List<String> listOfIds) {
        this.listOfIds = listOfIds;
    }
}
