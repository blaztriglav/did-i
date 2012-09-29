package si.modrajagoda.didi.database;

import java.sql.SQLException;

import si.modrajagoda.didi.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private static final String DATABASE_NAME = "habits.db";
	private static final int DATABASE_VERSION = 1;
	
	// the DAO object used to access the tables
	private Dao<Habit, Integer> habitDao;

	//DatabaseHelper includes config file that was generated by DatabaseConfigUtil 
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config); 
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Habit.class);
			TableUtils.createTable(connectionSource, Week.class);
			TableUtils.createTable(connectionSource, WeekDetails.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Habit.class, true);
			TableUtils.dropTable(connectionSource, Week.class, true);
			TableUtils.dropTable(connectionSource, WeekDetails.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
					+ newVer, e);
		}
	}
	
	/**
	 * Returns the Database Access Object (DAO) for table class. It will create it or just give the cached
	 * value.
	 */
	public Dao<Habit, Integer> getHabitDao() throws SQLException {
		if (habitDao == null) {
			habitDao = getDao(Habit.class);
		}
		return habitDao;
	}
	
	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		super.close();
		habitDao = null;
	}
}
