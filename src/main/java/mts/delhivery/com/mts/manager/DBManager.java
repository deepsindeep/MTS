package mts.delhivery.com.mts.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import mts.delhivery.com.mts.config.Config;
import mts.delhivery.com.mts.database.DaoMaster;
import mts.delhivery.com.mts.database.DaoSession;
import mts.delhivery.com.mts.database.Location;
import mts.delhivery.com.mts.database.LocationDao;
import mts.delhivery.com.mts.util.JsonUtil;
import mts.delhivery.com.mts.util.Tracer;


/**
 * Created by delhivery on 3/5/16.
 */
public class DBManager {

    private static final String TAG = Config.logger + DBManager.class.getSimpleName();
    private final static String DEFAULT = "DEFAULT";
    private static LocationDao sLocationDao;
    private  static Context mContext;



    /*
    * Initialization of DB and DaoSessiaons and tables
    * Dao object refers to corresponding Table
    *
    * */
    public static void init(Context context) {
        mContext = context;
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context, "mts", null);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Tracer.debug(TAG, " init " + " DB version" + db.getVersion());
        DaoSession daoSession = new DaoMaster(db).newSession();
        sLocationDao = daoSession.getLocationDao();
        Tracer.debug(TAG," init "+" DB created"+db.getPath());
    }

    public static void clearDB() {
        Tracer.debug(TAG, " clearDB " + " ");
    }

    public static void putLocation(Location location) {
        Tracer.debug(TAG," putLocation "+" "+ JsonUtil.jsonify(location));
        sLocationDao.insert(location);
        LocationSyncManager.getInstance(mContext).startSync();
    }

    public static long getLocationsCount() {
        return sLocationDao.count();
    }

    public static Location getLocation() {
        return sLocationDao.queryBuilder().orderAsc(LocationDao.Properties.Epoc).offset(0).limit(1).unique();
    }

    public static void removeLocation(Location location) {
        sLocationDao.delete(location);
    }

}
