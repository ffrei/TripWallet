package fr.hexaki.partywallet.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.hexaki.partywallet.activity.MainActivity;
import fr.hexaki.partywallet.db.elements.Consomation;
import fr.hexaki.partywallet.db.elements.Lien;
import fr.hexaki.partywallet.db.elements.MyDate;
import fr.hexaki.partywallet.db.elements.Personne;
import fr.hexaki.partywallet.db.elements.Sortie;

public class DatabaseHandler extends SQLiteOpenHelper {


	public static final String TAG=MainActivity.TAG ;
	
	public static final int PAYE_ET_RECU = 0;
	public static final int RECU = 1;
	public static final int PAYE = 2;
	
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "partyWallet";

	// Contacts table name
	private static final String TABLE_SORTIE = "sortie";
	private static final String TABLE_PERS = "personnes";
	private static final String TABLE_CONS = "consommations";
	private static final String TABLE_LIENS = "liens";

	// Contacts Table Columns names
	private static final String KEY_ID_SORTIE = "id_sortie";
	private static final String KEY_DESTINATIION = "dest_sortie";
	private static final String KEY_DATE = "date_sortie";
	private static final String KEY_DUREE = "duree_sortie"; // en jours
	
	private static final String KEY_ID_PERS = "id_pers";
	private static final String KEY_NAME = "nom";
	
	private static final String KEY_ID_CONS = "id_cons";
	private static final String KEY_LABEL = "labal";
	private static final String KEY_VAL = "montant";
	private static final String KEY_TYPE = "type";
	private static final String KEY_PAYEUR = "payeur";	// 3 val possible : cf recu et paye    


	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE = "CREATE TABLE " + TABLE_SORTIE + "("
				+ KEY_ID_SORTIE + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
				+ KEY_DATE +" DATETIME DEFAULT CURRENT_TIMESTAMP," 
				+ KEY_DUREE + " INTEGER," 
				+ KEY_DESTINATIION + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
		CREATE_TABLE = "CREATE TABLE " + TABLE_PERS + "("
				+ KEY_ID_PERS + " INTEGER PRIMARY KEY,"
				+ KEY_ID_SORTIE + " INTEGER,"
				+ KEY_NAME + " TEXT" + ")";
		db.execSQL(CREATE_TABLE);
		CREATE_TABLE = "CREATE TABLE " + TABLE_LIENS + "("
				+ KEY_ID_PERS + " INTEGER," 
				+ KEY_ID_CONS + " INTEGER,"
				+ KEY_PAYEUR +" INTEGER," 
				+	" PRIMARY KEY(" + KEY_ID_PERS + " ," + KEY_ID_CONS + "))";
		db.execSQL(CREATE_TABLE);
		CREATE_TABLE = "CREATE TABLE " + TABLE_CONS + "("
				+ KEY_ID_CONS + " INTEGER PRIMARY KEY,"
				+ KEY_ID_SORTIE + " INTEGER,"
				+ KEY_VAL + " REAL,"
				+ KEY_LABEL + " TEXT,"
				+ KEY_TYPE +" INTEGER"
				 + ")";
		db.execSQL(CREATE_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		reset(db);
	}

	public void reset() {
		SQLiteDatabase db = this.getWritableDatabase();
		reset(db);
	}
	
	public void reset(SQLiteDatabase db){
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIENS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SORTIE);
		// Create tables again
		onCreate(db);
	}
	

	// Adding new product
	public void addConsomation(Consomation c) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_VAL,c.montant);
		values.put(KEY_ID_SORTIE, c.id_sortie);
		values.put(KEY_LABEL,c.label);
		values.put(KEY_TYPE,c.type);
		c.id = (int)db.insert(TABLE_CONS, null, values);
		db.close(); // Closing database connection
	}
	
	public void addPersonne(Personne p){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME,p.nom);
		values.put(KEY_ID_SORTIE, p.id_sortie);
		p.id = (int) db.insert(TABLE_PERS, null, values);
		db.close(); // Closing database connection
	}
	
	public void addLiens(Lien l){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID_PERS,l.idPers);
		values.put(KEY_ID_CONS,l.idCons);
		values.put(KEY_PAYEUR,l.payeur);
		db.insert(TABLE_LIENS, null, values);
		db.close(); // Closing database connection
	}
	
	public void addSortie(Sortie s){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DESTINATIION,s.nom);
		values.put(KEY_DATE,s.date.toDate());
		values.put(KEY_DUREE,s.duree);
		s.id = (int) db.insert(TABLE_SORTIE, null, values);
		db.close(); // Closing database connection
	}
	
	

	// Getting single contact
	public Personne getPersonne(int id) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PERS, new String[] { KEY_ID_PERS,KEY_ID_SORTIE,
				KEY_NAME }, KEY_ID_PERS + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Personne p = new Personne(cursor.getInt(0),cursor.getInt(1),cursor.getString(2));

		cursor.close();
		db.close();

		return p;
	}

	
	

	
	public ArrayList<Personne> getPersonnes(int numSortie) {
		ArrayList<Personne> list = new ArrayList<Personne>();


		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PERS +" WHERE "+KEY_ID_SORTIE+"= \""+numSortie+"\" ";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add( new Personne(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return list;
	}
	

	public ArrayList<Sortie> getSorties() {
		ArrayList<Sortie> list = new ArrayList<Sortie>();


		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SORTIE;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				//public Sortie(int id,String n,MyDate d,int duree) {
				list.add( new Sortie(cursor.getInt(0),cursor.getString(3),new MyDate(cursor.getString(1)),cursor.getInt(2) )  );
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return list;
	}


	public ArrayList<Personne> getPersonnesPaye(int id_cons) {

		ArrayList<Personne> list = new ArrayList<Personne>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PERS + " NATURAL JOIN "+TABLE_LIENS 
							+" WHERE "+KEY_ID_CONS+"= \""+id_cons+"\" "
							+" AND "+KEY_PAYEUR +"<> \""+RECU+"\" " ;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add( new Personne(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return list;
	}
	
	public ArrayList<Personne> getPersonnesRecu(int id_cons) {

		ArrayList<Personne> list = new ArrayList<Personne>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PERS + " NATURAL JOIN "+TABLE_LIENS 
							+" WHERE "+KEY_ID_CONS+"= \""+id_cons+"\" "
							+" AND "+KEY_PAYEUR +"<> \""+PAYE+"\" " ;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add( new Personne(cursor.getInt(0),cursor.getInt(1),cursor.getString(2)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return list;
	}
	

	
	public Consomation getConsommation(int id){

		Consomation res=null;

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONS +" WHERE "+KEY_ID_CONS+"= \""+id+"\""; ;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor != null){
			cursor.moveToFirst();
			res = new Consomation(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4));
		}

		// return  list
		cursor.close();
		db.close();

		return res;
	}


	public ArrayList<Consomation> getConsomationsSorite(int id){

		ArrayList<Consomation> consos=new ArrayList<Consomation>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONS +" WHERE " +KEY_ID_SORTIE+"= \""+id+"\"";;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Log.d(TAG,"getConsomationsSorite "+cursor.getCount());
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				consos.add(new Consomation(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return consos;
	}
	
	public ArrayList<Consomation> getConsomations(int id) {

		ArrayList<Consomation> list = new ArrayList<Consomation>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONS + " NATURAL JOIN "+TABLE_LIENS +" WHERE "+KEY_ID_PERS+"= \""+id+"\"";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add(new Consomation(cursor.getInt(0), cursor.getInt(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();


		return list;
	}
	
	public void removeConsomation(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "DELETE FROM "+TABLE_LIENS + " WHERE "+KEY_ID_CONS+ "= \""+id+"\"";
		Log.d(TAG,"Exec querry : "+query);
		db.execSQL(query);
		query = "DELETE FROM "+TABLE_CONS + " WHERE "+KEY_ID_CONS+ "= \""+id+"\"";
		Log.d(TAG,"Exec querry : "+query);
		db.execSQL(query);
		Log.d(TAG,"Done");
	}

	

	public ArrayList<Lien> getLien() {

		ArrayList<Lien> list = new ArrayList<Lien>();

		// Select All Query
		String selectQuery = "SELECT  * FROM " +TABLE_LIENS ;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				list.add( new  Lien(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2)));
			} while (cursor.moveToNext());
		}

		// return  list
		cursor.close();
		db.close();

		return list;
	}

	public Sortie getSortie(int id) {
		
		Sortie res=null;
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SORTIE +" WHERE "+KEY_ID_SORTIE+"= \""+id+"\""; ;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor != null && cursor.getCount()==1){
			cursor.moveToFirst();
			try {
				
				int v_id=cursor.getInt(0);
				String dest = cursor.getString(3);
				MyDate date = new MyDate(cursor.getString(1));
				int duree = cursor.getInt(2);
				
				res =  new Sortie(v_id,dest,date,duree );
			} catch (Exception e) {
				Log.d(TAG,"getSortie "+e.getMessage());
			}
		}
		
		// return  list
		cursor.close();
		db.close();

		return res;

	}
	
	


}

























