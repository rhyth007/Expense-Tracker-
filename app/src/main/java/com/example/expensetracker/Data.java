package com.example.expensetracker;

public class Data {
    String item, date, id, notes,itemday,itemweek,itemmonth;
    int amount, month, week;


    public  Data(){}


    public Data(String item, String date, String id, String notes, String itemday, String itemweek, String itemmonth, int amount, int month, int week) {
        this.item = item;
        this.date = date;
        this.id = id;
        this.notes = notes;
        this.itemday = itemday;
        this.itemweek = itemweek;
        this.itemmonth = itemmonth;
        this.amount = amount;
        this.month = month;
        this.week = week;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getItemday() {
        return itemday;
    }

    public void setItemdat(String itemday) {
        this.itemday = itemday;
    }

    public String getItemweek() {
        return itemweek;
    }

    public void setItemweek(String itemweek) {
        this.itemweek = itemweek;
    }

    public String getItemmonth() {
        return itemmonth;
    }

    public void setItemmonth(String itemmonth) {
        this.itemmonth = itemmonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }



}