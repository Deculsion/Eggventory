package eggventory.logic.commands.edit;

import eggventory.model.StockList;
import eggventory.storage.Storage;
import eggventory.logic.commands.Command;
import eggventory.commons.enums.CommandType;
import eggventory.commons.enums.StockProperty;
import eggventory.model.items.Stock;
import eggventory.ui.Ui;


//@@author patwaririshab
/**
 * Command objects for editing stocks
 * Requires the stockCode (as listed by the system) of the stock.
 */
public class EditStockCommand extends Command {

    private String stockCode;
    private StockProperty property; //Stores the property you want to edit
    private String newValue; //Stores the newValue you want

    /**
     * Initializes all the attributes of the details of the stock to be edited.
     * @param type The type of command.
     * @param stockCode The unique String that identifies a Stock.
     * @param property The property of the Stock that is to be changed.
     * @param newValue The newValue of the property.
     */
    public EditStockCommand(CommandType type, String stockCode, StockProperty property, String newValue) {
        super(type);
        this.stockCode = stockCode;
        this.property = property;
        this.newValue = newValue;
    }

    /**
     * Executes the actual editing of the stock's property.
     * @param list StockList containing all the StockTypes.
     * @param ui Ui implementation to display output to.
     * @param storage  Storage object to handle saving and loading of any data.
     * @return String of the output, for JUnit testing.
     */
    @Override
    public String execute(StockList list, Ui ui, Storage storage) {
        String output;

        if (property == StockProperty.STOCKCODE && list.isExistingStockCode(newValue)) {
            output = String.format("Sorry, the stock code \"%s\" is already assigned to a stock in the system. "
                    + "Please enter a different stock code.", newValue);
            ui.print(output);
            return output;
        }

        Stock edited = list.setStock(stockCode, property, newValue);
        output = String.format("Awesome! I have successfully updated the following stock: %s | %s | %d | %s\n",
                edited.getStockType(), edited.getStockCode(), edited.getQuantity(),
                edited.getDescription());
        storage.save(list);
        ui.print(output);
        // Drawing stock data in GUI table.
        ui.drawTable(list.getAllStocksStruct());
        return output;
    }
}