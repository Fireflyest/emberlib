package io.fireflyest.emberlib.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import org.bukkit.scheduler.BukkitRunnable;
import io.fireflyest.emberlib.Print;

/**
 * 数据库连接关闭任务
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class ConnectCloseRunnable extends BukkitRunnable {

    private final String url;
    private final Connection connection;

    public ConnectCloseRunnable(@Nonnull String url, @Nonnull Connection connection) {
        this.url = url;
        this.connection = connection;
    }

    @Override
    public void run() {
        Print.EMBER_LIB.debug("Auto close connection: {}", url);
        try {
            if (!connection.isClosed()) {
                connection.isClosed();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
