package io.fireflyest.emberlib.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TestPage extends Page {

    protected TestPage(String target, int pageNumber, int size) {
        super(target, pageNumber, size);
        this.title = "test";
    }

    @Override
    public void refreshPage() {
        this.slot(pageNumber, new ItemStack(Material.SADDLE));
    }
    
}
