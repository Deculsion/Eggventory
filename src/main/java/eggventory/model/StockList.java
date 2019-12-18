package eggventory.model;

import eggventory.commons.enums.StockProperty;
import eggventory.commons.exceptions.BadInputException;
import eggventory.model.items.Stock;
import eggventory.model.items.StockType;
import eggventory.ui.TableStruct;

import java.util.ArrayList;
import java.util.Collections;

//@@author Deculsion
public class StockList {
    private ArrayList<Stock> stockList;

    /**
     * Constructs a new StockList object using an already existing stockList.
     * @param stockList ArrayList<> of StockType objects. There should already be a default "Uncategorised" StockType.
     */
    public StockList(ArrayList<Stock> stockList) {
        this.stockList = stockList;
    }

    /**
     * Constructs a new StockList object with one default StockType, "Uncategorised".
     */
    public StockList() {
        this.stockList = new ArrayList<>();
    }

    /**
     * Gets a particular StockType of the list based on the index.
     * @param i The index of the stocktype
     * @return the stocktype which the index references
     */
    public Stock get(int i) {
        return stockList.get(i);
    }

    /**
     * Gets the whole stockList. Note: technically doing using this method will violate OOP.
     * @return the list.
     */
    public ArrayList<Stock> getList() {
        return stockList;
    }

    //@@author cyanoei
    /**
     * Prints every stock within stocklist. Should only be called by Ui.
     * Deletes a StockType object, and all the stocks under it.
     * @param name Name of StockType to be deleted
     * @return The object if it was deleted, null if nothing waas deleted.
     */
    public Stock deleteStockType(String name) {
        Stock deleted;

        for (Stock stock : stockList) {
            if (stock.getStockType().equals(name)) {
                deleted = stock;
                stockList.remove(stock);
                return deleted;
            }
        }

        return null;
    }

    //@@author

    /**
     * Returns all stocks corresponding to a stockType specified.
     * @param stockType The unique string of the stockType of Stocks to search for
     * @return ArrayList of Stock objects
     */
    public ArrayList<Stock> getStockType(String stockType) {
        ArrayList<Stock> stocks = new ArrayList<>();
        for (Stock stock : stockList) {
            if (stock.getStockType().equals(stockType)) {
                stocks.add(stock);
            }
        }
        return stocks;
    }

    /**
     * Gets the total number of stockTypes in this stockList. Not to be confused with getStockQuantity.
     * @return the number of stockTypes.
     */
    public int getStockTypeQuantity() { //The number of stockTypes in the list.
        return stockList.size();
    }

    /**
     * Adds a Stock to the list.
     * @param stockType A String matching exactly the StockType to add the new Stock object under.
     * @param stockCode A unique String that identifies the Stock.
     * @param quantity Quantity of the stock.
     * @param description A String describing the nature of the Stock object.
     */
    public void addStock(String stockType, String stockCode, int quantity, String description)
            throws BadInputException {

        stockList.add(new Stock(stockType, stockCode, quantity, description));

    }

    //@@author cyanoei
    /**
     * Deletes a Stock object from a list.
     * @param stockCode The unique String that identifies a Stock.
     * @return the stock that was deleted, for printing purposes.
     */
    public Stock deleteStock(String stockCode) {
        Stock deleted = null;

        for (Stock stock : stockList) {
            deleted = stock;
        }

        return deleted;
    }

    /**
     * Formats an error message for the case of editing to a repeated stockCode.
     * @param newStockCode the new stockCode chosen by the user.
     * @return the error message.
     */
    public String repeatedStockCodeOutput(String newStockCode) {
        return String.format("Sorry, the stock code \"%s\" is already assigned to a stock in the system. "
                + "Please enter a different stock code.", newStockCode);
    }

    /**
     * Formats an error message for the case of trying to edit a nonexistent stockCode.
     * @param stockCode the stockCode which does not exist in the system.
     * @return the error message.
     */
    public String nonexistentStockCodeOutput(String stockCode) {
        return String.format("Sorry, the stock code \"%s\" cannot be found in the system. "
                + "Please enter a different stock code.", stockCode);
    }

    //@@author patwaririshab
    /**
     * Edits a stock in the stocklist.
     * @param stockCode The unique String that identifies a Stock.
     * @param property The attribute of the Stock that needs to be modified (Note: for now only 1).
     * @param newValue  The new value of the property we want to edit.
     * @return the stock before edits, for printing purposes.
     */
    public Stock setStock(String stockCode, StockProperty property, String newValue)
            throws BadInputException {
        Stock updatedStock = null;

        //Error: StockCode not found.
        if (!isExistingStockCode(stockCode)) {
            throw new BadInputException(nonexistentStockCodeOutput(stockCode));
        }

        //Error: New StockCode is already used.
        if (property == StockProperty.STOCKCODE && isExistingStockCode(newValue)) {
            throw new BadInputException(repeatedStockCodeOutput(newValue));
        }

        for (Stock stock : stockList) {
            if (stock.getStockCode().equals(stockCode)) {
                updatedStock = stock.setProperty(stockCode, property, newValue);
                break;
            }
        }
        return updatedStock;
    }

    /**
     * Edits the name of all stocks with the current stockTypeName to a newName.
     * @param stockTypeName The unique String that identifies a StockType.
     * @param newName The newName of the StockType.
     * @return An ArrayList of all stock objects changed.
     */
    public ArrayList<Stock> setStockType(String stockTypeName, String newName) {
        ArrayList<Stock> updated = new ArrayList<>();
        for (Stock stock : stockList) {
            if (stock.getStockType().equals(stockTypeName)) {
                stock.setStockType(newName);
                updated.add(stock);
            }
        }
        return updated;
    }

    /**
     * Gets the total number of stocks in this stockList. This sums the number of stocks across stockTypes.
     * @return the total number of stocks.
     */
    public int getTotalNumberOfStocks() { //The number of stocks in the list, across all stockTypes.
        return stockList.size();

    }

    //@@author cyanoei

    /**
     * Obtains the quantity of a Stock based on its StockCode.
     * @param stockCode the StockCode of the Stock.
     * @return the quantity of the stock, if found, otherwise -1. 
     */
    public int getStockQuantity(String stockCode) {
        Stock stock = findStock(stockCode);
        if (stock == null) {
            return -1;
        } else {
            return stock.getQuantity();
        }
    }


    /**
     * Determines if any of the stocks in this stockList have the same stockCode.
     * @param stockCode the queried stockCode.
     * @return true if a stock in this stockList has that stockCode and false if none of the stocks have this stockCode.
     */
    public boolean isExistingStockCode(String stockCode) {
        for (Stock stock : stockList) {
            if (stock.getStockCode().equals(stockCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the queried stockType already exists in the system.
     * @param stockTypeName the new name for a stockType that the user wants to add/edit.
     * @return true if the stockType is already implemented, false if it is new.
     */
    public boolean isExistingStockType(String stockTypeName) {
        for (Stock stock: stockList) {
            if (stock.getStockType().equals(stockTypeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for a stock in the stockList which has that stockCode.
     * @param stockCode the stockCode being queried.
     * @return the stock in question.
     */
    public Stock findStock(String stockCode) {
        for (Stock stock : stockList) {
            if (stock.getStockCode().equals(stockCode)) {
                return stock;
            }
        }
        return null;
    }

    //@@author yanprosobo
    /**
     * Returns whether there are any stocktypes stored in the list.
     */
    public boolean isEmpty() {
        return stockList.isEmpty();
    }

    /**
     * Clears the list of all elements.
     */
    public void clearList() {
        stockList.clear();
    }
// Not needed with removal of StockType class.
//    /**
//     * Checks if a given stocktype has no stocks with its stocktype.
//     * @param stockTypeName The name of the stocktype to be queried
//     * @return True if the stocktype has no stocks, false otherwise.
//     */
//    public boolean isStocktypeZeroQuantity(String stockTypeName) {
//        for (StockType stocktype: stockList) {
//            if (stocktype.getName().equals(stockTypeName) && isStockTypeEmpty(stocktype)) {
//                return true;
//            }
//        }
//        return false;
//    }

    /**
     * Prints every stock within stocklist whose stocktype matches query. Should only be called by Cli.
     * @pre Stocktype has been checked to exist and contains at least one stock.
     * @return The string of the stocktype whose stocktype matches query.
     */
    public String queryStocks(String query) {
        StringBuilder ret = new StringBuilder();
        ret.append(query).append(" INVENTORY\n");
        ret.append("------------------------\n");
        for (Stock stock : stockList) {
            if (stocktype.getName().equals(query)) {
                ret.append(stocktype.toString()).append("\n");
            }
        }
        return ret.toString();
    }

    /**
     * Prints all the stocktypes that are currently handled by Eggventory. Should only be called by Cli.
     * @return The string of all the stocktypes
     */
    public String toStocktypeString() {
        StringBuilder ret = new StringBuilder();
        ret.append("LISTING STOCKTYPES\n");
        for (StockType stocktype : stockList) {
            ret.append("------------------------\n");
            ret.append(stocktype.getName()).append("\n");
        }
        return ret.toString();
    }

    /**
     * Prints every stock within stocklist. Should only be called by Cli.
     * @return The string of the stocklist.
     */
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("CURRENT INVENTORY\n");

        for (StockType stocktype : stockList) {
            if (stocktype.toString() != "") { //Does not print empty StockTypes.
                ret.append("------------------------\n");
                ret.append(stocktype.toString()).append("\n");
            }
        }

        return ret.toString();
    }

    public boolean isStockTypeEmpty(StockType stocktype) {
        return (stocktype.getQuantity() == 0);
    }


    /**
     * Saves the list into a String.
     * @return The String that will be directly saved into file.
     */
    public String saveDetailsString() {
        StringBuilder details = new StringBuilder();
        for (StockType stocktype : stockList) {
            if (isStockTypeEmpty(stocktype) == false) {
                details.append(stocktype.saveDetailsString()); //Don't need to add newline.
            }
        }

        return details.toString();
    }

    //@@author patwaririshab
    /**
     * Saves the stocktypes into a String.
     * @return The String will be directly saved into a saved_stocktypes file.
     */
    public String saveStockTypesString() {
        StringBuilder stockTypesString = new StringBuilder();

        for (StockType stocktype : stockList) {
            stockTypesString.append(stocktype.getName()).append("\n");
        }
        return stockTypesString.toString();
    }

    //@@author Raghav-B
    /**
     * Returns TableStruct containing data on all stocks contained by StockList. This
     * TableStruct is read by the GUI table.
     * @return TableStruct with data.
     */
    public TableStruct getAllStocksStruct() {
        TableStruct tableStruct = new TableStruct("Stock List");
        tableStruct.setTableColumns("Stock Type", "Stock Code", "Total", "Description", "Minimum", "Loaned");

        ArrayList<ArrayList<String>> dataArray = new ArrayList<>();
        for (StockType stockType : stockList) {
            dataArray.addAll(stockType.getDataAsArray());
        }
        tableStruct.setTableData(dataArray);

        return tableStruct;
    }

    /**
     * Returns TableStruct containing data on all stocktypes contained by StockList. This
     * TableStruct is read by the GUI table.
     * @return TableStruct with data.
     */
    public TableStruct getAllStockTypesStruct() {
        TableStruct tableStruct = new TableStruct("Stocktype List");
        tableStruct.setTableColumns("Stock Type");

        ArrayList<ArrayList<String>> dataArray = new ArrayList<>();
        for (StockType stockType : stockList) {
            dataArray.add(new ArrayList<>(Collections.singletonList(stockType.getName())));
        }
        tableStruct.setTableData(dataArray);

        return tableStruct;
    }

    /**
     * Returns TableStruct containing data on all stocks under a specific stocktype. This
     * TableStruct is read by the GUI table.
     * @param stockTypeName Name of stocktype under which all stocks will be listed.
     * @return TableStruct with data.
     */
    public TableStruct getAllStocksInStockTypeStruct(String stockTypeName) {
        TableStruct tableStruct = new TableStruct("Stock List: " + stockTypeName);
        tableStruct.setTableColumns("Stock Type", "Stock Code", "Quantity", "Description", "Minimum", "Loaned");

        ArrayList<ArrayList<String>> dataArray = new ArrayList<>();
        for (StockType stockType : stockList) {
            if (stockType.getName().equals(stockTypeName)) {
                dataArray.addAll(stockType.getDataAsArray());
            }
        }
        tableStruct.setTableData(dataArray);

        return tableStruct;
    }
    //@@author

}
