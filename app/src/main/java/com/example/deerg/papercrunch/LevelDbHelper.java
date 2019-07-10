package com.example.deerg.papercrunch;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;



public class LevelDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="level_db";

    public static final int DATABASE_VERSION= 1;

    public static final String CREATE_TABLE="create table level (level_id integer primary key,level_num text,level_name text ,progress integer,image integer)";
    public static final String CREATE_TABLE2="create table sublevel (sub_level_id integer primary key,sublevel_name text,concept1 text,concept2 text,concept3 text,level_id integer)";
    public static final String CREATE_TABLE3="create table subbool (sub_level_id integer primary key,sub_status integer,current_level integer)";

    public static final String DROP_TABLE ="drop table if exists level ";
    public static final String DROP_TABLE2 ="drop table if exists sublevel ";
    public static final String DROP_TABLE3 ="drop table if exists subbool ";

    public LevelDbHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

        Log.d("Database Operations","Database created.. ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db=this.getWritableDatabase();
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE2);
        db.execSQL(CREATE_TABLE3);
        putLevel(db);
        putsubLevel(db);
        putsubbool(db);
        Log.d("Databaase Op","Table created");
    }//Creates tables and adds content.

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db=this.getWritableDatabase();
        db.execSQL(DROP_TABLE);
        db.execSQL(DROP_TABLE2);
        db.execSQL(DROP_TABLE3);
        onCreate(db);
        db.close();
    }//Upgrades the tables.

    public void checktable(SQLiteDatabase db){

        db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from sqlite_master", null);
        if(cursor.getCount()==0) {
            onCreate(db);
        }
    }//Checks the existence of non-empty tables.

    public void addLevel(int id,String levelnum,String level,int progress,int img,SQLiteDatabase db){
        //db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("level_id",id);
        contentValues.put("level_name",level);
        contentValues.put("level_num",levelnum);
        contentValues.put("progress",progress);
        contentValues.put("image",img);

        db.insert("level",null,contentValues);

    }//Adds row of contents to level table.


    public int getprogress(SQLiteDatabase db,int id){
        int prog=0;
        db=this.getReadableDatabase();
        String level = Integer.toString(id);
        Cursor cursor = db.rawQuery("select progress from level where level_id= ?",new String[]{level});
        cursor.moveToFirst();
        prog=cursor.getInt(cursor.getColumnIndex("progress"));
        return prog;
    }//Returns the progress of particular level.

    public CardData readLevel(int id,SQLiteDatabase db) {
        List<String> lvl=new ArrayList<String>();
        db= this.getReadableDatabase();
        String level = Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from level where level_id = ?",new String[]{level});
        cursor.moveToFirst();
        //int cnt=0;
        String lvlname;
        String lvlnum;
        int prog;
        int img;
        int back;

        lvlname = cursor.getString(cursor.getColumnIndex("level_name"));
        lvlnum = cursor.getString(cursor.getColumnIndex("level_num"));
        prog = cursor.getInt(cursor.getColumnIndex("progress"));
        img = cursor.getInt(cursor.getColumnIndex("image"));
        int curr =1;
        curr = getcurrlev(db);
        if(curr==id){
            back =R.drawable.current;
        }
        else{back = R.drawable.card;}

        return new CardData(lvlnum,lvlname,prog,img,id,back);
    }//Returns CardData object of particular level.

    public void changeprogress(int id,SQLiteDatabase db,int prog){
        String level=Integer.toString(id);
        String progress=Integer.toString(prog);
        db=this.getWritableDatabase();
        db.execSQL("update level set progress = ? where level_id =  ? ",new String[]{progress,level});
    }// Updates progress of particular level.

    public void putLevel(SQLiteDatabase db){
        //db= this.getWritableDatabase();
        addLevel(1,"Level 1","Introduction",0,R.drawable.ic_content_paste_black_24dp,db);
        addLevel(2,"Level 2","Data Types and Variables",0,R.drawable.ic_date_range_black_24dp,db);
        addLevel(3,"Level 3","Operators",0,R.drawable.ic_developer_mode_black_24dp,db);
        addLevel(4,"Level 4","Input/Output",0,R.drawable.ic_input_black_24dp,db);
        addLevel(5,"Level 5","Logical Operators",0,R.drawable.logic,db);
        addLevel(6,"Level 6","Conditional Statements",0,R.drawable.ic_swap_horiz_black_24dp,db);
        addLevel(7,"Level 7","Loops",0,R.drawable.ic_loop_black_24dp,db);
        addLevel(8,"Level 8","Functions",0,R.drawable.ic_functions_black_24dp,db);
        addLevel(9,"Level 9","Arrays and Strings",0,R.drawable.ic_view_array_black_24dp,db);
    }//Adds contents to the table.


    public List<String> getprev(SQLiteDatabase db, int id){
        List<String> lev =new ArrayList<String>();
        db= this.getReadableDatabase();
        String level=Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from level where level_id < ?",new String[]{level});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            lev.add(cursor.getString(cursor.getColumnIndex("level_name")));
            cursor.moveToNext();

        }
        return lev;
    }//Returns all the previous levels.


    public void addsubbool(int id,int status,int currlev,SQLiteDatabase db){
        //db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sub_level_id",id);
        contentValues.put("sub_status",status);
        contentValues.put("current_level",currlev);
        db.insert("subbool",null,contentValues);
    }//Adds contents in subbool Table.

    public void putsubbool(SQLiteDatabase db){
        addsubbool(1,0,1,db);
        addsubbool(2,0,1,db);
        addsubbool(3,0,1,db);
        addsubbool(4,0,1,db);
        addsubbool(5,0,1,db);
        addsubbool(6,0,1,db);
        addsubbool(7,0,1,db);
        addsubbool(8,0,1,db);
        addsubbool(9,0,1,db);
        addsubbool(10,0,1,db);
        addsubbool(11,0,1,db);
        addsubbool(12,0,1,db);
        addsubbool(13,0,1,db);
        addsubbool(14,0,1,db);
        addsubbool(15,0,1,db);
        addsubbool(16,0,1,db);
        addsubbool(17,0,1,db);
        addsubbool(18,0,1,db);
        addsubbool(19,0,1,db);
        addsubbool(20,0,1,db);
        addsubbool(21,0,1,db);
        addsubbool(22,0,1,db);
        addsubbool(23,0,1,db);
        addsubbool(24,0,1,db);
        addsubbool(25,0,1,db);
        addsubbool(26,0,1,db);
        addsubbool(27,0,1,db);







    }//Adds contents in the table.

    public int getlevid(String lvlname,SQLiteDatabase db){
        int id;
        db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select level_id from level where level_name=?",new String[]{lvlname});
        cursor.moveToFirst();
        id = cursor.getInt(cursor.getColumnIndex("level_id"));
        return id;
    }//Returns level id using the Text Level name.

    public int getbool(int id,SQLiteDatabase db){
        int bool;

        db=this.getReadableDatabase();
        String level = Integer.toString(id);
        Cursor cursor = db.rawQuery("select sub_status from subbool where sub_level_id= ?",new String[]{level});
        cursor.moveToFirst();
        bool=cursor.getInt(cursor.getColumnIndex("sub_status"));

        return bool;
    }// Returns the status of sublevel.

    public void updatebool(int id,SQLiteDatabase db,int bool){
        String sublevel=Integer.toString(id);
        String boole =Integer.toString(bool);
        db=this.getWritableDatabase();
        db.execSQL("update subbool set sub_status = ? where sub_level_id =  ? ",new String[]{boole,sublevel});

    }//Updates status of particular sublevel.


    public int getcurrlev(SQLiteDatabase db){
        int lev;
        db=this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select current_level from subbool",null);
        cursor.moveToFirst();
        lev=cursor.getInt(cursor.getColumnIndex("current_level"));
        return lev;
    }//Returns current level of user from subbool table.

    public void updatecurrlev(SQLiteDatabase db,int bool){
        String boole = Integer.toString(bool);
        db=this.getWritableDatabase();
        db.execSQL("update subbool set current_level=?",new String[]{boole});
    }//Updates the current level.

    public void addsubLevel(int id,String sublevel1,String concept1,String concept2,String concept3,int level_id,SQLiteDatabase db){
//        db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sub_level_id",id);
        contentValues.put("sublevel_name",sublevel1);
        contentValues.put("concept1",concept1);
        contentValues.put("concept2",concept2);
        contentValues.put("concept3",concept3);
        contentValues.put("level_id",level_id);

        db.insert("sublevel",null,contentValues);

    }//Adds row of contents in sublevel table.

    public List<String> readSubLevel(SQLiteDatabase db,int id){
        List<String> lev =new ArrayList<String>();
        db= this.getReadableDatabase();
        String level=Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from sublevel where level_id = ?",new String[]{level});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            lev.add(cursor.getString(cursor.getColumnIndex("sublevel_name")));
            cursor.moveToNext();
        }
        return lev;
    }//Reads sublevel table and returns the sublevels.

    public List<String> getconcept1(SQLiteDatabase db, int id){
        List<String> lev =new ArrayList<String>();
        db= this.getReadableDatabase();
        String level=Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from sublevel where level_id = ?",new String[]{level});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            lev.add(cursor.getString(cursor.getColumnIndex("concept1")));
            cursor.moveToNext();
        }
        return lev;
    }// Returns concept1 of every sublevel.

    public List<String> getconcept2(SQLiteDatabase db, int id){
        List<String> lev =new ArrayList<String>();
        db= this.getReadableDatabase();
        String level=Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from sublevel where level_id = ?",new String[]{level});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            lev.add(cursor.getString(cursor.getColumnIndex("concept2")));
            cursor.moveToNext();
        }
        return lev;
    }// Returns concept2 of every sublevel.


    public List<String> getconcept3(SQLiteDatabase db, int id){
        List<String> lev =new ArrayList<String>();
        db= this.getReadableDatabase();
        String level=Integer.toString(id);
        Cursor cursor = db.rawQuery("select * from sublevel where level_id = ?",new String[]{level});
        cursor.moveToFirst();
        while(cursor.isAfterLast()==false)
        {
            lev.add(cursor.getString(cursor.getColumnIndex("concept3")));
            cursor.moveToNext();
        }
        return lev;
    }// Returns concept3 of every sublevel.

    public int readSubid(String subname,SQLiteDatabase db)// Returns sublevevl id.
    {
        db=this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT sub_level_id FROM sublevel WHERE sublevel_name = ?",new String[]{subname});
        int a;
        cursor.moveToFirst();
        a=cursor.getInt(cursor.getColumnIndex("sub_level_id"));
        return a;

    }

    public void putsubLevel(SQLiteDatabase db){
        //db = this.getWritableDatabase();
        addsubLevel(1,"What is Programming?","We all have heard about Computer Programming gaining a lot of popularity in the past 3 decades. " +
                "But what is programming? Programming is simply a way to \"instruct the computer to perform various tasks\".","This basically means that you " +
                "provide the computer a set of instructions that are written in a language that the computer can understand.","Just like we humans can understand" +
                " a few languages (English, Spanish, Mandarin, French, etc.), so is the case with computers. Computers understand instructions that are written in a specific " +
                "syntactical form called a programming language.",1,db);
        addsubLevel(2,"What is C?","C is a procedural programming language.","It was initially developed by Dennis Ritchie as a system " +
                "programming language to write operating system.","The main features of C language include \n" +
                "low-level access to memory, simple set of keywords, and clean style, these features make C language suitable for system programming.",1,db);
        addsubLevel(3,"Basic C syntax","*return data type* main()\n" +
                "{\n" +
                "\t*Executable commands*;\n" +
                "\treturn *value*;\n" +
                "}\n" +
                "The function called at program startup is named main.","The implementation declares no prototype for this function. It shall be defined with a" +
                " return type of int and 1:\nwith no parameters:\n" +
                "int main(void) { /* ... */ }","2:\n with two parameters (referred to here as argc and argv, though any names may be used, as they are local to the " +
                "function in which they are declared):\n" +
                "int main(int argc, char *argv[]) { /* ... */ }or equivalent;10) or in some other implementation-defined manner.\n" +
                "We will understand what is the meaning of all this in some time.",1,db);
        addsubLevel(4,"My first C program","Lets have a look at how to print a statement \"Hello World\" on the screen using C program.\n" +
                "int main()\n" +
                "{\n" +
                "\tprintf(\"Hello, World\\n\");\n" +
                "\treturn 0;\n" +
                "}","Here \"printf\" is the command which we use to print something on the screen. It is equivalent to saying, \"Hey computer, print this text\". " +
                "In this program we are returning the value \"0\", which\n" +
                "means that the program ran fine.","In case of an error during the execution of the program, it won't return 0 and we'll know that something is wrong.\n" +
                "\"\\n\" tells the compiler that the cursor should now be moved to the next line. If we do not put \\n then the cursor will stay in that position only and " +
                "everything we print after this will get printed in one line.",1,db);
        addsubLevel(5,"What are Data Types?","Data types in computer programming is the type of data stored anywhere.","For example, let's " +
                "assume that there is a memory location that stores the value '2' and there is another location that stores \n" +
                "the value 'Hello world'.","Now these two data are not similar and it helps the computer if we tell what kind of data it is that we are storing. " +
                "Here is where the concept of data types comes in." ,2,db);
        addsubLevel(6,"What are variables?","Variable is a named location in memory where a program can store and manipulate the data. If we are " +
                        "storing a value in any memory location, we might want to use it in our program further. We do this by\n" +
                        "\t\t\tassigning a name to that memory location and use that name to refer to that memory location. \n" +
                        "In C programs, the value of the variable may get changed in the program. It might belong to any of the data type like float, int, char, etc.",
                "ItHowever, we cannot give any name to a C variables. \n" +
                        "Here are some rules \n" +
                        "regarding naming the variable:\n" +
                        "1)Variable name must begin with a letter or an underscore.\n" +
                        "2)Variables are case sensitive.\n" +
                        "3)They can be constructeed with digits, letters.\t\t\n" +
                        "4)No special symbols are allowed other than underscore.\n" +
                        "For example: sun, moon505, height, __value are some examples for variable name.",
                "--Examples\n" +
                        "The folllowing line creates a varibale called number with the value 10.\n" +
                        "\t-- int number;\n" +
                        "The following is also a valid variable name.\n" +
                        "\t-- float decimal_number;\n" +
                        "They can also have captial letters.\n" +
                        "\t-- int Number;\n" +
                        "\t-- float decimal_Number;\n" +
                        "They can also have numbers.\n" +
                        "\t-- int number1;\n" +
                        "\t-- float number2;\n" +
                        "But they can't start with numbers. For example these are invalid variable names :\n" +
                        "\t-- int 1number;\n" +
                        "t-- float 2number;\n" +
                        "t-- char 3letter;",2,db);
        addsubLevel(7,"Data Types in C","The following are the data types that are supported by C:\n" +
                        "\t1)int: The variable with data type 'int' can store all the integer values. It requires 4 bytes of memory.\n" +
                        "\t2)char:The most basic data type in C. It stores a single character and requires a single byte of memory in almost all compilers. For ex: 'a', 'e'\n" +
                        "\t3)float: It is used to store decimal numbers (numbers with floating point value) with single precision.\n" +
                        "\t4)double: It is used to store decimal numbers (numbers with floating point value) with double precision. \n" +
                        "We can use the sizeof() operator to check the size of a variable. sizeof(int) will return 4 bytes, sizeof(char) will return 1 byte and so on.",
                "Variable declaration and initialization:\n" +
                        "Variable declaration means picking up a memory location of a particular size and assigning a name to it. This location now referred to by this " +
                        "name can be later used to store a value. Variable declaration is done by mentioning the data type of the value it can store along with the variable name.\n" +
                        "Variable initialization means assigning a value to the variable. Remember, we can only assign the value whose data type matches with the data " +
                        "type of the variable. ",
                "Examples\n" +
                        "\tFor integer:\n" +
                        "\tAll the following examples store the value in the variable number as 10, since number can store only integrs.\n" +
                        "\tSo, any decimal value is also truncated to an integer.\n" +
                        "\t-- int number = 10;\n" +
                        "\t-- int number = 10.1;\n" +
                        "\n" +
                        "\tFor float:\n" +
                        "\tAll the following examples store the value in the variable number as decimals, since number can store only decimal values.\n" +
                        "\tSo, any integer value is also converted to a decimal.\n" +
                        "\t-- float number = 3.14;\n" +
                        "\t-- float number = 10; (Stored as 10.0)\n" +
                        "\n" +
                        "\tFor char:\n" +
                        "\tAll the following examples store the value in the variable number alphabets.\n" +
                        "\t-- char letter = 'A';\n" +
                        "\t-- char letter = 'z';\n" ,2,db);

        addsubLevel(8,"What are Operators?","Operators are the foundation of any programming language. Thus the functionality of C/C++ programming" +
                        " language is incomplete without the use of operators.",
                "We can define operators as symbols that helps us to perform " +
                        "specific mathematical and logical computations on operands. In other words we can say that an operator operates the operands.",
                "For example,\n" +
                        "\tc = a + b;\n" +
                        "Here, \"+\" is the operator known as addition operator and \"a\" and \"b\" are operands. The addition operator tells the compiler to add " +
                        "both of the operands \"a\" and \"b\"." ,3,db);
        addsubLevel(9,"Operators list","C has many built-in operator types which can be classified as follows:\n" +
                        "ARTHEMATIC OPERATORS:These are the operators used to perform arithmetic/mathematical operations on operands. Examples: (+, -, *, /, %,++,\")." +
                        " Arithmetic operator are of two types:\n" +
                        "1) Unary Operators: Operators that operates or works with a single operand are unary operators.\n" +
                        "For example: (++ , --)\n" +
                        "++ operator increments the value of the associated variable by 1\n" +
                        "-- operator decreases the value of the associated variable by 1\n" +
                        "These operators can be placed before or after the variable\n" +
                        "\n" +
                        "2) Binary Opeators: Operators that operates or works with two operands are binary operators.\n" +
                        "For example: (+ , - , * , /)\n" +
                        "+ performs addition of the two variable, - performs subtraction, * performs multiplication and / performs divisioin. \n" +
                        "'%' is called the modulous operator. It pervides the remainder after dividing the number to the laft by that to its right.",
                " RELATIONAL OPERATOR: Logical Operators are used to combine two or more conditions/constraints or to complement the evaluation of the " +
                        "original condition in consideration. The result of the operation of a logical operator \n" +
                        "is a boolean value either true or false.\nASSIGNMENT OPERATOR:  Assignment operators are used to assign value to a variable. The left side" +
                        " operand of the assignment operator is a variable and right side operand of the assignment operator is a value. The value on \\n\" +\n" +
                        "\"the right side must be of the same data-type of variable on the left side otherwise the compiler will raise an error.\\n\" +\n" +
                        "\"'=': Assign the value on the right to the variable on the left.",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "Example 1:\n" +
                        "#include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "int a = 5,b = 10, c;\n" +
                        "\n" +
                        "c = a+b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c = a-b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c = a*b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c=a/b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c=a%b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result:\n" +
                        "15\n" +
                        "-5\n" +
                        "50\n" +
                        "0 (Note the 0 and not 0.5)\n" +
                        "Remainder when a divided by b=5\n" +
                        "\n" +
                        "Example 2:\n" +
                        "#include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "float a = 5,b = 10, c;\n" +
                        "\n" +
                        "c = a+b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c = a-b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c = a*b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "c=a/b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result:\n" +
                        "15.000000 \n" +
                        "-5.000000 \n" +
                        "50.000000 \n" +
                        "0.500000 (Note the 0.5. This is because, the opearands have to be decimal values in order for the result to be decimal in case of division)\n" +
                        "\n" +
                        "Example 3:\n" +
                        "#include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "float a = 5,b = 10, c;\n" +
                        "\n" +
                        "c=a%b;\n" +
                        "printf(c);\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result:\n" +
                        "Produces an error because modulus operations cannot be applied on decimal values." ,3,db);
        addsubLevel(10,"Input and Output","While writing a computer program, there are a lot of instances where it is necessary to demand an " +
                        "input from the user using the program for his purpose and display a result.",
                "If such features aren't there, then the computer \n" +
                        "program is not user friendly. C language has standard libraries that allow input and output in a program.",
                "The stdio.h or standard input output library in C that has methods for input and output." ,4,db);

        addsubLevel(11,"scanf() and printf()","scanf():\n" +
                        "The scanf() method, in C, reads the value from the console as per the type specified.\n" +
                        "The syntax of a scanf() is:\n" +
                        "scanf(\"%X\", &variableOfXType);\n" +
                        "where %X is the format specifier in C. It is a way to tell the compiler what type of data is in a variable\n" +
                        "& is the address operator in C, which tells the compiler to change the real value of this variable, stored at this address in the memory.\n" +
                        "printf():\n" +
                        "The printf() method, in C, prints the value passed as the parameter to it, on the console screen.\n" +
                        "Syntax:\n" +
                        "printf(\"%X\", variableOfXType);\n",
                "Taking input and output of basic types in C\n" +
                        "\n" +
                        "For integer input/output:\n" +
                        "Input: scanf(\"%d\", &intVariable);\n" +
                        "Output: printf(\"%d\", intVariable);\t\t\t\n" +
                        "For float input/output:\n" +
                        "Input: scanf(\"%f\", &floatVariable);\n" +
                        "Output: printf(\"%f\", floatVariable);\t\t\t\n" +
                        "For character input/output:\n" +
                        "Input: scanf(\"%c\", &charVariable);\n" +
                        "Output: printf(\"%c\", charVariable);",
                "--Examples:\n" +
                        "\n" +
                        "\n" +
                        "Example 1: \n" +
                        "Taking Input\n" +
                        "Taking input as integer\n" +
                        "#include<stdio.h>\n" +
                        "int main(){\n" +
                        "int number;\n" +
                        "\n" +
                        "scanf(\"%d\", &number);\n" +
                        "}\n" +
                        "\n" +
                        "Taking input as float\n" +
                        "#include<stdio.h>\n" +
                        "int main(){\n" +
                        "float decimal;\n" +
                        "\n" +
                        "scanf(\"%f\", &decimal);\n" +
                        "}\n" +
                        "\n" +
                        "Taking input as char\n" +
                        "#include<stdio.h>\n" +
                        "int main(){\n" +
                        "char letter;\n" +
                        "\n" +
                        "scanf(\"%c\", &letter);\n" +
                        "}\n" +
                        "\n" +
                        "Example 2:\n" +
                        "Taking multiple inputs at once\n" +
                        "\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int number; \n" +
                        "float decimal;\n" +
                        "char letter;\n" +
                        "\n" +
                        "scanf(\"%d %f %c\", &number, &decimal, &letter);\n" +
                        "}\n" +
                        "\n" +
                        "Example 3:\n" +
                        "Printing\n" +
                        "\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int number=5; \n" +
                        "float decimal=5.0;\n" +
                        "char letter='k';\n" +
                        "\n" +
                        "printf(\"%d %f %c\", &number, &decimal, &letter);\n" +
                        "}\n" +
                        "\n" +
                        "The output will be '5 5.0 k'" ,4,db);

        addsubLevel(12,"Relational operators","They are used for comparison of two values. This operator always returns a true or false" +
                        " valueLet\"s see them one by one:\n'==': checks whether the two given operands are equal or not. If so, it returns TRUE.\n" +
                        "'!=': checks whether the two given operands are equal or not. If so, it returns FALSE.",
                "'>': checks whether the first operand is greater than the second operand. If so, it returns true.\n" +
                        "'<': checks whether the first operand is lesser than the second operand. If so, it returns true.\n" +
                        "'>=': checks whether the first operand is greater than or equal to the second operand. If so, it returns true.\n" +
                        "'<=' operator checks whether the first operand is lesser than or equal to the second operand. If so, it returns true.",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i) Let 'a' and 'b' be two variables.\n" +
                        "Then the following operators can be used as following:\n" +
                        "\n" +
                        "'==' : a == b\n" +
                        "'!=' : a != b\n" +
                        "'>'  : a > b\n" +
                        "'<'  : a < b\n" +
                        "'>=' : a >= b\n" +
                        "'<=' : a <= b" ,5,db);
        addsubLevel(13,"Logical Operators","They are used to combine two or more conditions/constraints or to complement the evaluation of the " +
                        "original condition in consideration.",
                "These operators also return value as TRUE or FALSE.They are described below:\nLogical AND: The '&&' operator returns true when both " +
                        "the conditions in consideration are satisfied. Otherwise it returns false.\n" +
                        "Logical OR: The '||' operator returns true when one (or both) of the conditions in consideration is satisfied. Otherwise it returns false.\n" +
                        "Logical NOT: The '!' operator returns true the condition in consideration is not satisfied. Otherwise it returns false.",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "Let 'a' and 'b' be two variables.\n" +
                        "Then the following operators can be used as following:\n" +
                        "\n" +
                        "'&&' : (a == 2) && (b == 3)\n" +
                        "'||' : (a == 2) || (b == 3)\n" +
                        "'!'  : !(a == b)" ,5,db);

        addsubLevel(14,"Conditional Statements","There comes situations in real life when we need to make some decisions and based on these decisions," +
                        " we decide what should we do next.",
                "Similar situations arises in programming also where we need to make some decisions and based on these decision we will execute the next block of code." +
                        " Decision making statements in programming languages decides the direction of flow of program execution.",
                "Decision making statements in C++ are if, if-else, nested if and switch." ,6,db);

        addsubLevel(15,"IF","if statement is the most simple decision making statement. It is used to decide whether a certain statement" +
                        " or block of statements will be executed or not i.e if a certain condition is true then a block of statement is executed otherwise not.",
                "Syntax:\n" +
                        "if(condition) \n" +
                        "{\n" +
                        "// Statements to execute if\n" +
                        "// condition is true\n" +
                        "}\n" +
                        "Here, condition after evaluation will be either true or false. if statement accepts boolean values \" if the value is true then it will " +
                        "execute the block of statements below it otherwise not.",
                "If we do not provide the curly braces \"{\" and \"}\" after if( condition ) then by default if statement will consider the first " +
                        "immediately below statement to be inside its block.\n" +
                        "\n" +
                        "--Examples\n" +
                        "\n" +
                        "\n" +
                        "Example 1:\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "\tint x = 10;\n" +
                        "\tif ((x%2 == 0) && (x < 15)){\n" +
                        "\t\tprintf(\"The condiion explains how '&&' works\");\n" +
                        "}\n" +
                        "\treturn 0;\n" +
                        "}\n" +
                        "\n" +
                        "Example 2:\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "\tint x = 20;\n" +
                        "\tint y = 22;\n" +
                        "\tif (x<y){\n" +
                        "\t\tprintf(\"Variable x is less than y\");\n" +
                        "\t}\n" +
                        "\treturn 0;\n" +
                        "}" ,6,db);

        addsubLevel(16,"IF......else","The if statement alone tells us that if a condition is true it will execute a block of statements " +
                        "and if the condition is false it won\"t. But what if we want to do something else if the condition is false.",
                "Here comes the else statement. We can use the else statement with if statement to execute a block of code when the condition is false.\nSyntax:\n" +
                        "\n" +
                        "if (condition)\n" +
                        "{\n" +
                        "// Executes this block if\n" +
                        "// condition is true\n" +
                        "}\n" +
                        "else\n" +
                        "{\n" +
                        "// Executes this block if\n" +
                        "// condition is false\n" +
                        "}",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "float x = 9.8;\n" +
                        "if (x>10){\n" +
                        "printf(\"This is how 'if' statements work\");\n" +
                        "}\n" +
                        "else{\n" +
                        "printf(\"This is how 'else' statement works\");\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "ii)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int x = 100;\n" +
                        "int y = 69;\n" +
                        "if (x == y){\n" +
                        "printf(\"Variable x is equal to y\");\n" +
                        "} else{\n" +
                        "printf(\"Variable x is not equal to y\");\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}" ,6,db);

        addsubLevel(17,"if..else if..else","Here, a user can decide among multiple options. The if statements are executed from the top down.\n" +
                        "As soon as one of the conditions controlling the if is true, the statement associated with that if is executed, and the rest of the " +
                        "ladder is bypassed. If none of the conditions is true, then the final else statement will be executed.",
                "Syntax:\n" +
                        "if (condition)\n" +
                        "statement;\n" +
                        "else if (condition)\n" +
                        "statement;\n" +
                        ".\n" +
                        ".\n" +
                        "else\n" +
                        "statement;",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int x = 10;\n" +
                        "if (x>10){\n" +
                        "printf(\"x is greater than 10\");\n" +
                        "} elseif(x<10){\n" +
                        "printf(\"x is less than 10\");\n" +
                        "} else{\n" +
                        "printf(\"x is equal to 10\");\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}" ,6,db);

        addsubLevel(18,"Switch","Switch case statements are a substitute for long if statements that compare a variable to several " +
                        "integral values. The switch statement is a multiway branch statement. It provides an easy way to dispatch execution to different " +
                        "parts of code based on the value of the expression. Switch is a control statement that allows a value to change control of execution.",
                "Syntax:\n" +
                        "\n" +
                        "switch (n)\n" +
                        "{\n" +
                        "case 1: // code to be executed if n = 1;\n" +
                        "break;\n" +
                        "case 2: // code to be executed if n = 2;\n" +
                        "break;\n" +
                        "default: // code to be executed if n doesn't match any cases\n" +
                        "}",
                "The following should be kept in mind while working with switch:\n" +
                        "1)The expression provided in the switch should result in a constant value otherwise it would not be valid. \n" +
                        "2)Duplicate case values are not allowed.\n" +
                        "3)The default statement is optional.\n" +
                        "4)The break statement is used inside the switch to terminate a statement sequence. When a break statement is reached, the switch " +
                        "terminates, and the flow of control jumps to the next line following the switch statement.\n" +
                        "5)The break statement is optional. If omitted, execution will continue on into the next case. The flow of control will fall through to " +
                        "subsequent cases until a break is reached.\n" +
                        "\n" +
                        "\n" +
                        "--Examples\n" +
                        "\n" +
                        "\n" +
                        "#include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "int i=2;\n" +
                        "switch (i)\n" +
                        "{\n" +
                        "case 1:\n" +
                        "printf(\"Case1 \");\n" +
                        "break;\n" +
                        "case 2:\n" +
                        "printf(\"Case2 \");\n" +
                        "break;\n" +
                        "case 3:\n" +
                        "printf(\"Case3 \");\n" +
                        "break;\n" +
                        "case 4:\n" +
                        "printf(\"Case4 \");\n" +
                        "break;\n" +
                        "default:\n" +
                        "printf(\"Default \");\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}" ,6,db);

        addsubLevel(19,"What does looping mean?","A for loop is a repetition control structure which allows us to write a loop that is " +
                        "executed a specific number of times. The loop enables us to perform n number of steps together in one line.\n" +
                        "Syntax:\n" +
                        "\n" +
                        "for (initialization expr; test expr; update expr)\n" +
                        "{    \t\n" +
                        "// body of the loop\n" +
                        "// statements we want to execute\n" +
                        "}",
                "In for loop, a loop variable is used to control the loop. First initialize this loop variable to some value, then check whether this variable" +
                        " is less than or greater than counter value. If statement is true, then loop body is executed \n" +
                        "and loop variable gets updated . Steps are repeated till exit condition comes.",
                "A 'break' statement is used to terminate the loop and the control will fall to the statements lying next to the loop structure. A 'continue'" +
                        " statement is used to skip that ineration of the loop and continue execution from the next round." ,7,db);

        addsubLevel(20,"For loop","In computer programming, a loop is a sequence of instructions that is repeated until a certain condition is reached.",
                "Loops in programming comes into use when we need to repeatedly execute a block of statements. For example: Suppose we want " +
                        "to print \"Hello World\" 10 times. Ther are two ways we can do this. First is to use printf() 10 times and the other method is to use it " +
                        "once and ask the computer to execute that line 10 times.\nInitialization Expression: In this expression we have to initialize the loop " +
                        "counter to some value. for example: int i=1;\n" +
                        "Test Expression: In this expression we have to test the condition. If the condition evaluates to true then we will execute the body of " +
                        "loop and go to update expression otherwise we will exit from the for loop. For example: i <= 10;\n" +
                        "Update Expression: After executing loop body this expression increments/decrements the loop variable by some value. for example: i++;",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int i;\n" +
                        "for (i=1; i<=3; i++){\n" +
                        "printf(\"%d\\n\", i);\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result: 1\n" +
                        "2\n" +
                        "3\n" +
                        "\n" +
                        "ii)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "for (int i=0; i<2; i++)\n" +
                        "{\n" +
                        "for (int j=0; j<4; j++)\n" +
                        "{\n" +
                        "printf(\"%d, %d\\n\",i ,j);\n" +
                        "}\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result: 0, 0\n" +
                        "0, 1\n" +
                        "0, 2\n" +
                        "0, 3\n" +
                        "1, 0\n" +
                        "1, 1\n" +
                        "1, 2\n" +
                        "1, 3" ,7,db);

        addsubLevel(21,"While","While studying for loop we have seen that the number of iterations is known beforehand, i.e. the number of " +
                        "times the loop body is needed to be executed is known to us.\n'While' loops are used in situations where we do not know the exact number of " +
                        "iterations of loop beforehand. The loop execution is terminated on the basis of test condition.",
                "Syntax:\n" +
                        "while (test_expression)\n" +
                        "{\n" +
                        "// statements\n" +
                        "\n" +
                        "update_expression;\n" +
                        "}",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int count=1;\n" +
                        "while (count <= 4)\n" +
                        "{\n" +
                        "printf(\"%d \", count);\n" +
                        "count++;\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result: 1 2 3 4\n" +
                        "\n" +
                        "ii)\n" +
                        "#include <stdio.h>\n" +
                        "int main(){\n" +
                        "int i=1, j=1;\n" +
                        "while (i <= 4 || j <= 3)\n" +
                        "{\n" +
                        "printf(\"%d %d\\n\",i, j);\n" +
                        "i++;\n" +
                        "j++;\n" +
                        "}\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result: 1 1\n" +
                        "2 2\n" +
                        "3 3\n" +
                        "4 4" ,7,db);

        addsubLevel(22,"DO While","In do-while loops also the loop execution is terminated on the basis of test condition. The main " +
                        "difference between do while loop and while loop is in do while loop the condition is tested at the end of loop body, i.e " +
                        "do while loop is exit controlled whereas the other two loops are entry controlled loops.",
                "Note: In do while loop the loop body will execute at least once irrespective of test condition.\nSyntax:\n" +
                        "initialization expression;\n" +
                        "do\n" +
                        "{\n" +
                        "// statements\n" +
                        "\n" +
                        "update_expression;\n" +
                        "} while (test_expression);\n" +
                        "\n" +
                        "Note: Notice the semi colon(\";\") in the end of loop.",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)\n" +
                        "#include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "int j=0;\n" +
                        "do\n" +
                        "{\n" +
                        "printf(\"Value of variable j is: %d\\n\", j);\n" +
                        "j++;\n" +
                        "}while (j<=3);\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result: \n" +
                        "Value of variable j is: 0\n" +
                        "Value of variable j is: 1\n" +
                        "Value of variable j is: 2\n" +
                        "Value of variable j is: 3\n" ,7,db);

        addsubLevel(23,"What are functions?","A function is a set of statements that take inputs, do some specific computation " +
                        "and produces output. The idea is to put some commonly or repeatedly done task together and make a function, so that instead of " +
                        "writing the same code again and again for different inputs, we can call the function.",
                "Everything in C is contained inside a function. Till now we have only been in one function, the 'main' function.  Every C program " +
                        "has a function called main() that is called by operating system when a user runs the program.",
                "Every function has a return type. If a function doesn\"t return any value, then void is used as return type.Moreover if the return " +
                        "type of the function is void ,we still can use return statement in the body of function definition by not specifying any constant,variable,etc. with \n" +
                        "it ,by only mentioning the \"return;\" statement which would symbolise the termination of the function." ,8,db);

        addsubLevel(24,"Writing your first function","Function declaration tells compiler about number of parameters function takes, " +
                        "data-types of parameters and return type of function. Putting parameter names in function declaration is optional in function " +
                        "declaration, but it is necessary to put them in definition. For example:\n" +
                        "int max(int, int);\n" +
                        "// A function that takes two integers as parameters \n" +
                        "// and returns an integer \n" +
                        "int *swap(int*,int); \n" +
                        "// A function that takes a int pointer and an int variable as parameters \n" +
                        "// and returns an integer of type int\nPassing Parameters to functions\n" +
                        "Parameters in functions are the values that are fed into the function before the execution of the functions actually begins." +
                        " It is like an input for the function on which it is supposed to work and return a value.\n" +
                        "The parameters passed to function are called actual parameters.\n" +
                        "The parameters received by function are called formal parameters.",
                "Whenever we want to execute a function while we are in another function, we make a call to that function. We 'call' the " +
                        "function by using the name of the function and pass the values we want to pass into that function.\n" +
                        "For ex:\n" +
                        "Let's say there is a function int max(int, int) which returns the greater number of the two numbers passed into it. So to " +
                        "call that function, we would use something like max(5, 10) and the value we should get is the greater number: 10.\n" +
                        "int a = max(5, 10); would store 10 inside a if the function max functions properly.\nPrototype of a function:\n" +
                        "*return type* *function name* (*parameters*){\n" +
                        "//statements\n" +
                        "return *value*;\n" +
                        "}\n" +
                        "Lets say that we want a function that will compute the square of number and return that value. Lets say the parameter " +
                        "that is passed is integer type\n" +
                        "So the value returned by the function will also be integer.\n" +
                        "\n" +
                        "int square(int a): function declaration\n" +
                        "Now in the body, we just have to calculate the square of a\n" +
                        "\n" +
                        "int b = a * a;:  function body\n" +
                        "Now we just return the calculated value b\n" +
                        "return b;\n" +
                        "\n" +
                        "So this is how our function will look like:\n" +
                        "\n" +
                        "int square(int a){\n" +
                        "int b = a * a;\n" +
                        "return b;\n" +
                        "}\n" +
                        "\n" +
                        "Now let's say we call this function in main:\n" +
                        "int sq = square(5)\n" +
                        "sq will store value 25 now.",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)\n" +
                        "#include<stdio.h>\n" +
                        "\n" +
                        "void hello(){\n" +
                        "printf(\"Hello World!!\");\n" +
                        "}\n" +
                        "\n" +
                        "int main(){\n" +
                        "hello()\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result : Hello World!!\n" +
                        "\n" +
                        "ii)\n" +
                        "#include<stdio.h>\n" +
                        "\n" +
                        "void number(int x){\n" +
                        "printf(\"The number you entered is %d\", x);\n" +
                        "}\n" +
                        "\n" +
                        "int main(){\n" +
                        "int num = 10;\n" +
                        "\n" +
                        "number(num);\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result : The number you entered is 10\n" +
                        "\n" +
                        "iii)\n" +
                        "#include<stdio.h>\n" +
                        "int add(int x, int y){\n" +
                        "int sum = x + y;\n" +
                        "return sum;\n" +
                        "}\n" +
                        "\n" +
                        "int main(){\n" +
                        "int num1 = 10;\n" +
                        "int num2 = 5;\n" +
                        "\n" +
                        "int num3 = add(10, 5);\n" +
                        "\n" +
                        "printf(\"The sum of %d and %d is %d\", num1, num2, num3);\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Result : The sum of 10 and 5 is 15" ,8,db);

        addsubLevel(25,"What is an array?","An array is collection of items stored at continuous memory locations.",
                "We can use normal variables (v1, v2, v3, ..) when we have small number of objects, but if we want to store large number of instances," +
                        " it becomes difficult to manage\n" +
                        "them with normal variables.",
                "The idea of array is to represent many instances in one variable." ,9,db);

        addsubLevel(26,"Arrays in C","In C, we can declare an array by just specifying its type and size or by initializing it or by both.\t\n" +
                        "Array declaration by initializing elements: For ex: int arr1[10]; Here we are declaring an array which will have all integer values " +
                        "and will hold a maximum of 10 values.\n" +
                        "Array declaration by initializing elements: For ex: int arr[] = { 10, 20, 30, 40 } \n" +
                        "Array declaration by specifying size and initializing elements: For ex: int arr[6] = { 10, 20, 30, 40 }",
                "In C, every array has what is called index. These are numbers which are used to point to a particular element of an array." +
                        " The first element of an array will have an index of 0. The second elements index would be 1, the third, 2 and so on.\n" +
                        "The index of the last element would be equal to one less than the length of the array.\nArray elements are accessed by using an" +
                        " integer index. Array index starts with 0 and goes till size of array minus 1.\n" +
                        "To access nth index element of array 'arr': arr[n]\n" +
                        "For ex: \n" +
                        "int arr[5]; \n" +
                        "arr[0] = 5; \n" +
                        "arr[2] = -10;\n" +
                        "We first declare an array arr with 5 elements maximum. We then assign value 5 to the index '0' and -10 to the index '2' .",
                "--Examples\n" +
                        "\n" +
                        "\n" +
                        "i)Index of an array is always an integer. If we use a floating constant, the compiler would only consider the integer part of the number. \n" +
                        "\n" +
                        "Input:\n" +
                        "#include <stdio.h> \n" +
                        "\n" +
                        "int main() \n" +
                        "{ \n" +
                        "int arr[5]; \n" +
                        "arr[0] = 5; \n" +
                        "arr[2] = -10; \n" +
                        "arr[3 / 2] = 2; // this is same as arr[1] = 2 \n" +
                        "arr[3] = arr[0]; \n" +
                        "\n" +
                        "printf(\"%d %d %d %d\", arr[0], arr[1], arr[2], arr[3]); \n" +
                        "\n" +
                        "return 0; \n" +
                        "}\n" +
                        "\n" +
                        "Output:\n" +
                        "5 2 -10 5\n" +
                        "\n" +
                        "ii)  C program to demonstrate that array elements are stored at contiguous locations\n" +
                        "\n" +
                        "Input:\n" +
                        "#include <stdio.h> \n" +
                        "int main() \n" +
                        "{ \n" +
                        "// an array of 10 integers.  If arr[0] is stored at \n" +
                        "// address x, then arr[1] is stored at x + sizeof(int) \n" +
                        "// arr[2] is stored at x + sizeof(int) + sizeof(int) \n" +
                        "// and so on. \n" +
                        "int arr[5], i; \n" +
                        "\n" +
                        "printf(\"Size of integer in this compiler is %lu\\n\", sizeof(int)); \n" +
                        "\n" +
                        "for (i = 0; i < 5; i++) \n" +
                        "// The use of '&' before a variable name, yields \n" +
                        "// address of variable. \n" +
                        "printf(\"Address arr[%d] is %p\\n\", i, &arr[i]); \n" +
                        "\n" +
                        "return 0; \n" +
                        "}\n" +
                        "\n" +
                        "Output:\n" +
                        "Size of integer in this compiler is 4\n" +
                        "Address arr[0] is 0x7ffd636b4260\n" +
                        "Address arr[1] is 0x7ffd636b4264\n" +
                        "Address arr[2] is 0x7ffd636b4268\n" +
                        "Address arr[3] is 0x7ffd636b426c\n" +
                        "Address arr[4] is 0x7ffd636b4270\n" +
                        "\n" +
                        "iii) Remember the 'sizeof()' operator. It is used to find the size of the parameter inside the parenthesis in bits. \n" +
                        "\n" +
                        "Input:\n" +
                        "# include <stdio.h>\n" +
                        "void print(int arr[])\n" +
                        "{\n" +
                        "int n = sizeof(arr)/sizeof(arr[0]);\n" +
                        "int i;\n" +
                        "for (i = 0; i < n; i++)\n" +
                        "printf(\"%d \", arr[i]);\n" +
                        "}\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "int arr[] = {1, 2, 3, 4, 5, 6, 7, 8};\n" +
                        "print(arr);\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Output:\n" +
                        "1 2 3 4 5 6 7 8\n" +
                        "\n" +
                        "iv) Whenever we do not assign any value to an index of an array, C itself assigns a value to that location. It is called garbage value\n" +
                        "\n" +
                        "Input:\n" +
                        "int main()\n" +
                        "{\n" +
                        "int i;\n" +
                        "int arr[5] = {1};\n" +
                        "for (i = 0; i < 5; i++)\n" +
                        "printf(\"%d \", arr[i]);\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Output:\n" +
                        "1 followed by 4 garbage values\n" +
                        "\n" +
                        "v)Remember that any number other that 0 is considered as 'TRUE' by C. Only 0 is considered as 'FALSE'\n" +
                        "\n" +
                        "Input:\n" +
                        "\n" +
                        "#include \"stdio.h\"\n" +
                        "\n" +
                        "int main()\n" +
                        "{\n" +
                        "int size = 4;\n" +
                        "int arr[size];\n" +
                        "if(arr[0])\n" +
                        "printf(\"Initialized to ZERO\");\n" +
                        "else\n" +
                        "printf(\"Not initialized to ZERO\");\n" +
                        "\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Output:\n" +
                        "It can be either \"Initialized to ZERO\" or \"Not initialized to ZERO\" depending upon the value C stores in the 0th index of the array" ,9,db);

        addsubLevel(27,"Strings","Strings are defined as an array of characters. The difference between a character array and a " +
                        "string is the string is terminated with a special character \"\\0\".\n" +
                        "Declaring a string is as simple as declaring a one dimensional array. Below is the basic syntax for declaring a string.\n" +
                        "char str_name[size];\nIn the above syntax str_name is any name given to the string variable and size is used define the length " +
                        "of the string, i.e the number of characters strings will store. Please keep in mind that there is an extra terminating character" +
                        " which is the Null character (\"\\0\") used to indicate termination of string which differs strings from normal character arrays.",
                "A string can be initialized in different ways. We will explain this with the help of examples.\n" +
                        "\n" +
                        "1) char str[] = \"Helloworld\";\n" +
                        "2) char str[50]=  \"Helloworld\";\n" +
                        "3) char str[] = {'H','e','l','l','o','w','o','r','l','d','/0'}\n" +
                        "\n" +
                        "For the input and output of strings, the following format is used:\n" +
                        "scanf(\"%s\", str); //where str is the name of the array of characters\n" +
                        "printf(\"%s\", str)",
                "--Example\n" +
                        "\n" +
                        "\n" +
                        "i)As we had discussed, any string is just an array. The identifier for a string is a '\\0' next to the last character of " +
                        "the string which is added by default by C. If there is no '\\0', the string is just treated as an ordinary array of characters.\n" +
                        "\n" +
                        "Input:\n" +
                        "# include <stdio.h>\n" +
                        "int main()\n" +
                        "{\n" +
                        "char str1[] = \"GeeksQuiz\";\n" +
                        "char str2[] = {'G', 'e', 'e', 'k', 's', 'Q', 'u', 'i', 'z'};\n" +
                        "int n1 = sizeof(str1)/sizeof(str1[0]);\n" +
                        "int n2 = sizeof(str2)/sizeof(str2[0]);\n" +
                        "printf(\"n1 = %d, n2 = %d\", n1, n2);\n" +
                        "return 0;\n" +
                        "}\n" +
                        "\n" +
                        "Ouput:\n" +
                        "n1 = 10, n2 = 9\n" +
                        "\n" +
                        "ii)\n" +
                        "\n" +
                        "Input:\n" +
                        "\n" +
                        "char p[20]; \n" +
                        "char s = \"string\"; \n" +
                        "int length = strlen(s);   //strlen() is a function which returns the length of the string\n" +
                        "int i; \n" +
                        "for (i = 0; i < length; i++) \n" +
                        "p[i] = s[length — i - 1]; \n" +
                        "printf(\"%s\", p);\n" +
                        "\n" +
                        "Output:\n" +
                        "\n" +
                        "gnirts\n" +
                        "\n" +
                        "iii) char * is just another method of declaring a string. It is what we call a character pointer. This data type holds a " +
                        "value that will point to the location which holds the first index of out string. To get the pointer of any variable, " +
                        "we use the symbol '&' before the variable.\n" +
                        "\n" +
                        "Input:\n" +
                        "\n" +
                        "char *a = \"Hello, world\";\n" +
                        "printf(\"%s\", a); //Note that we haven't used & while writing a. This is because a is already a pointer\n" +
                        "\n" +
                        "Output:\n" +
                        "\n" +
                        "Hello, world" ,9,db);

    }//Adds contents to the sublevel table.

}

