package com.as4.galaxyfunpark;


public class item_object {

    private String item_Id ;
    private String item_name ;
    private String item_Barcode ;
    private String item_Status ;
    private String imagePath ;
    private String Extra;
    public void item_object()
    {

        item_Barcode="";
        item_name="";
        item_Status="";
        item_Id="";

    }



    public void setname(String name)
    {
        item_name = name;
    }
    public String getname( )
    {
        return item_name ;
    }

    public void setID(String Id)
    {
        item_Id = Id;
    }
    public String getID( )
    {
        return item_Id ;
    }



    public void setBarcode(String Barcode)
    {


        item_Barcode = Barcode;
    }
    public String getBarcode( )
    {
        return item_Barcode ;
    }


    public void setStatus(String Status)
    {

        item_Status = Status;
    }
    public String getStatus( )
    {


        return item_Status ;
    }






}
