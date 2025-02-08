package io.fireflyest.emberlib.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * 测试页面
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TestPage extends Page {

    protected TestPage(String target, int pageNumber, int size) {
        super(target, pageNumber, size);
        this.title = "test";
    }

    @Override
    public void refreshPage() {

        this.slot(0, new ItemStack(Material.SADDLE));
        this.slot(1, new ItemStack(Material.SADDLE));
        this.slot(2, new ItemStack(Material.SADDLE));
        this.slot(3, new ItemStack(Material.SADDLE));
        this.slot(4, new ItemStack(Material.SADDLE));
        this.slot(5, new ItemStack(Material.SADDLE));
        this.slot(6, new ItemStack(Material.SADDLE));
        this.slot(7, new ItemStack(Material.SADDLE));
        this.slot(8, new ItemStack(Material.STONE));
    }
    
}
