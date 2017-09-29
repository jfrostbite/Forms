package com.e_eduspace.identify;

/**
 * 需求： 获取点范围，根据Coolpen 点击2次获取一个区域范围坐标
 * <p>
 * 思路：２点确定一个矩形。将2个点封装在制定该功能集合中。来判断点是否处于该功能范围内
 */

class PointTool {

    /*private static final String TAG = "PointTool";
    private static boolean isRun;

    private static final ArrayBlockingQueue<Runnable> QUEUE = new ArrayBlockingQueue<Runnable>(3);

    private static List<PointAera> aeras = new ArrayList<>();

    //识别集合
    private static List<List<NoteStroke>> exe_ones = new ArrayList<>();
    private static List<List<NoteStroke>> exe_twos = new ArrayList<>();
    private static List<List<NoteStroke>> grammars = new ArrayList<>();
    private static List<List<NoteStroke>> translations = new ArrayList<>();

    //答案集合
    private static List<String> exe_one_result = new ArrayList<>();
    private static List<String> exe_two_result = new ArrayList<>();
    private static List<String> grammar_result = new ArrayList<>();
    private static List<String> translation_result = new ArrayList<>();
    private static List<Integer> selects = new ArrayList<>();

    private static boolean processing;

    private static final String EXE_ONE = "exercise_one";
    private static final int EXE_ONE_SIZE = 6;

    private static final String EXE_TWO = "exercise_two";
    private static final int EXE_TWO_SIZE = 9;

    private static final String[] auditions = {
            "audition_1"
            , "audition_2"
            , "audition_3"
            , "audition_4"
            , "audition_5"};
    private static final int AUDITION_ANSWER_SIZE = 3;
    private static final int AUDITION_SIZE = 12;

    private static final String[] reads = {
            "read_1"
            , "read_2"
            , "read_3"
            , "read_4"
            , "read_5"
            , "read_6"
            , "read_7"};
    private static final int READ_SIZE = 4;

    private static final String GRAMMAR = "grammar";
    private static final int GRAMMAR_SIZE = 10;

    private static final String TRANSLATION = "translation";
    private static final int TRANSLATION_SIZE = 2;

    private static ThreadPoolExecutor executor;
    private static CHAPTER chapter;
    private static onDataChangedListener listener;
    private static int current = -1;

    static {
        initData();
        initView();
//        loadData();
    }

    private static void initList(List<List<NoteStroke>> lists, int size) {
        lists.clear();
        for (int i = 0; i < size; i++) {
            lists.add(new ArrayList<NoteStroke>());
        }
    }


    private static void initResultList(List<String> grammar_result, int size) {
        grammar_result.clear();
        for (int i = 0; i < size; i++) {
            grammar_result.add("");
        }
    }

    private static SingleLineWidget slw;

    private static void initData() {
        AssetManager assets = AppApplication.getApp().getResources().getAssets();
        try {
            InputStream is = assets.open("pointdb");
            File path = AppApplication.getApp().getFilesDir();
            File file = new File(path, "point.db");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = is.read(buf)) > -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
            sd = SQLiteDatabase.openDatabase(file.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
            executor = new ThreadPoolExecutor(20, 25, 200, TimeUnit.MINUTES, QUEUE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void initView() {
        slw = new SingleLineWidget(AppApplication.getApp());
        if (slw.registerCertificate(MyCertificate.getBytes())) {
            Log.e("aaaaa", "===========证书注册成功==========");
        } else {
            Log.e("aaaaa", "===========证书注册失败==========");
        }

        slw.addSearchDir("zip://" + AppApplication.getApp().getPackageCodePath() + "!/assets/conf");
        slw.clear();
        slw.configure("zh_CN", "cur_text");

        slw.setOnTextChangedListener(new SingleLineWidgetApi.OnTextChangedListener() {
            @Override
            public void onTextChanged(SingleLineWidgetApi widgetApi, String s, boolean b) {
                if (b) {
                    return;
                }
                processing = b;
                if (slw.getTag() instanceof Integer) {
                    int tag = (int) slw.getTag();
                    switch (current) {
                        case 0:
                            exe_one_result.set(tag, s);
                            if (tag == EXE_ONE_SIZE - 1) {//处理下一个
//                                Log.e(TAG, "~~~~~~~~~~~~~~~~~");
//                                for (String s1 : exe_one_result) {
//                                    Log.e(TAG, s1);
//                                }
                                PointTool.listener.onDataChanged(exe_one_result);
                            }
                            break;
                        case 1:
                            exe_two_result.set(tag, s);
                            if (tag == EXE_TWO_SIZE - 1) {//处理下一个
//                                Log.e(TAG, "~~~~~~~~~~~~~~~~~");
//                                for (String s1 : exe_two_result) {
//                                    Log.e(TAG, s1);
//                                }
                                PointTool.listener.onDataChanged(exe_two_result);
                            }
                            break;
                        case 2:
                            grammar_result.set(tag, s);
                            if (tag == GRAMMAR_SIZE - 1) {//处理下一个
//                                Log.e(TAG, "~~~~~~~~~~~~~~~~~");
//                                for (String s1 : grammar_result) {
//                                    Log.e(TAG, s1);
//                                }
                                PointTool.listener.onDataChanged(grammar_result);
                            }
                            break;
                        case 3:
                            translation_result.set(tag, s);
                            if (tag == TRANSLATION_SIZE - 1) {//处理完成
//                                Log.e(TAG, "~~~~~~~~~~~~~~~~~");
//                                for (String s1 : translation_result) {
//                                    Log.e(TAG, s1);
//                                }
                                PointTool.listener.onDataChanged(translation_result);
                            }
                            break;
                    }
                }
            }
        });
    }

    private static SQLiteDatabase sd;

    *//**
     * 测点范围用
     *
     * @param point
     *//*
    private static void addPoint(NotePoint point) {
        Float px = point.getPX();
        Float py = point.getPY() + 207;
        Float press = point.getPress();

        if (px > 1222f && px < 1239f && py > 396f && py < 419f) {
//            save();
//            loadData();
            return;
        }
        *//*isRun = false;
        if (press > 0) {
            list.add(point);
        } else {
            if (list.isEmpty()) {
                return;
            }
            NotePoint result = validPoint(list);
            PointAera aera = new PointAera();
            aera.setX(result.getPX());
            aera.setY(result.getPY());
            aera.setLoc(i++);
            aera.setTag("translation");
            aeras.add(aera);
            Log.e("POINTAERA", aeras.size() +  "======"+ aera.getLoc() + "");
            list.clear();
        }*//*
    }

    private static void save() {
        if (isRun) {
            return;
        }
        DaoSession dao = DatabaseManager.getInstance(AppApplication.getApp(), Constant.DB_NAME).getDaoSession();
        PointAeraDao aera = dao.getPointAeraDao();
        for (PointAera pointAera : aeras) {
            aera.insert(pointAera);
        }
        aeras.clear();
        Log.e("POINTAERA", aeras.size() + "======保存");
        isRun = true;
    }

    *//**
     * 加载数据库数据
     *//*
    public static void loadData(CHAPTER chapter, onDataChangedListener listener) throws Exception{
        PointTool.chapter = chapter;
        PointTool.listener = listener;

        init();
        DaoSession daoSession = DatabaseManager.getInstance(AppApplication.getApp(), Constant.DB_NAME)
                .getDaoSession();
        NoteStrokeDao noteStrokeDao = daoSession.getNoteStrokeDao();
        List<NoteStroke> noteStrokes = null;
        if (noteStrokeDao != null) {
            noteStrokes = noteStrokeDao.loadAll();
        }
        final List<NoteStroke> finalNoteStrokes = noteStrokes;
        if (finalNoteStrokes != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < finalNoteStrokes.size(); i++) {
                        decodeNoteList(finalNoteStrokes.get(i));
                    }
                    try {
                        convert();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void init() {
        current = -1;
        exe_one_result.clear();
        exe_two_result.clear();
        grammar_result.clear();
        translation_result.clear();
        initList(exe_ones, EXE_ONE_SIZE);
        initList(exe_twos, EXE_TWO_SIZE);
        initList(grammars, GRAMMAR_SIZE);
        initList(translations, TRANSLATION_SIZE);

        initResultList(exe_one_result, EXE_ONE_SIZE);
        initResultList(exe_two_result, EXE_TWO_SIZE);
        initResultList(grammar_result, GRAMMAR_SIZE);
        initResultList(translation_result, TRANSLATION_SIZE);


        selects.clear();
        for (int i = 0; i < 12; i++) {
            selects.add(-1);
        }
    }

    private static void decodeNoteList(NoteStroke noteStroke) {
        if (iDentify(EXE_ONE, EXE_ONE_SIZE, noteStroke)) {
            return;
        }
        if (iDentify(EXE_TWO, EXE_TWO_SIZE, noteStroke)) {
            return;
        }
        for (int i = 0, len = auditions.length; i < len; i++) {
            String audition = auditions[i];
            int select = select(audition, AUDITION_ANSWER_SIZE, noteStroke);
            if (select >= 0) {
                selects.set(i, select);
                return;
            }
        }

        for (int i = 0, len = reads.length; i < len; i++) {
            String read = reads[i];
            int select = select(read, READ_SIZE, noteStroke);
            if (select >= 0) {
                selects.set(i, select);
                return;
            }
        }

        if (iDentify(GRAMMAR, GRAMMAR_SIZE, noteStroke)) {
            return;
        }
        if (iDentify(TRANSLATION, TRANSLATION_SIZE, noteStroke)) {
            return;
        }
    }

    *//**
     * 识别用
     *
     * @param tag
     * @param size
     * @param noteStroke
     * @return
     *//*
    private static boolean iDentify(String tag, int size, NoteStroke noteStroke) {
        boolean matching = false;
        for (int i = 0; i < size; i++) {
            if (judge(tag, i, noteStroke)) {
                matching = true;
                switch (tag) {
                    case EXE_ONE:
                        exe_ones.get(i).add(noteStroke);
                        break;
                    case EXE_TWO:
                        exe_twos.get(i).add(noteStroke);
                        break;
//            case AUDITION_1:
//                break;
//            case AUDITION_2:
//                break;
//            case AUDITION_3:
//                break;
//            case AUDITION_4:
//                break;
//            case AUDITION_5:
//                break;
//            case READ_1:
//                break;
//            case READ_2:
//                break;
//            case READ_3:
//                break;
//            case READ_4:
//                break;
//            case READ_5:
//                break;
//            case READ_6:
//                break;
//            case READ_7:
//                break;
                    case GRAMMAR:
                        grammars.get(i).add(noteStroke);
                        break;
                    case TRANSLATION:
                        translations.get(i).add(noteStroke);
                        break;
                    default:
                        break;

                }
            }
        }
        return matching;
    }

    *//**
     * 根据点获取所有数据
     *
     * @return
     *//*
    private static <E> void convert() throws Exception{
        if (CHAPTER.EXE_ONE.equals(chapter)) {
            process(exe_ones, 0);
        } else if (CHAPTER.EXE_TWO.equals(chapter)) {
            process(exe_twos, 1);
        } else if (CHAPTER.GRAMMAR.equals(chapter)) {
            process(grammars, 2);
        } else if (CHAPTER.TRANSLATION.equals(chapter)) {
            process(translations, 3);
        } else if (CHAPTER.AUDITION.equals(chapter)) {
            listener.onDataChanged(selects);
        }
    }

    private static void process(final List<List<NoteStroke>> lists, final int tag) {
        current = tag;
        for (int i = 0; i < lists.size(); i++) {
            if (processing) {//正在识别
                Log.e(TAG, "正在识别...");
                i--;
                SystemClock.sleep(1000);
                continue;
            } else {
                slw.clear();
            }

            slw.setTag(i);
            //区域识别
            List<NoteStroke> exe_oneList = lists.get(i);
            for (NoteStroke noteStroke : exe_oneList) {
                List<CaptureInfo> infos = noteStroke.getCaptureInfoArray();
                slw.addStroke(infos);
                processing = true;
            }
        }
        processing = false;
    }

    private static int select(String tag, int size, NoteStroke noteStroke) {
        for (int i = 0; i < size; i++) {
            if (judge(tag, i, noteStroke)) {
                return i;
            }
        }
        return -1;
    }

    public static void test(NotePoint point, onDataChangedListener listener) {

        Float px = point.getPX();
        Float py = point.getPY() + 207;
        Float press = point.getPress();

        if (px > 1222f && px < 1239f && py > 396f && py < 419f) {
//            save();
//            loadData();
            return;
        }

        *//*Cursor cursor = sd.query("POINT_AERA", new String[]{"x", "y"}, "tag = ? and loc = ?", new String[]{EXE_ONE, 0 + ""}, null, null, null);

        List<PointAera> aeras = new ArrayList<>();
        while (cursor.moveToNext()) {
            PointAera aera = new PointAera();
            int xindex = cursor.getColumnIndex("X");
            float x = cursor.getFloat(xindex);
            int yindex = cursor.getColumnIndex("Y");
            float y = cursor.getFloat(yindex);
            aera.setX(x);
            aera.setY(y);
            aeras.add(aera);
        }*//*
    }

    private static boolean judge(String tag, int loc, NoteStroke noteStroke) {
        if (noteStroke == null) {
            throw new RuntimeException("参数错误");
        }
        NotePoint point = validPoint(noteStroke.getNotePointArray());

        Float px = point.getPX();
        Float py = point.getPY() + 207;

        Cursor cursor = sd.query("POINT_AERA", new String[]{"x", "y"}, "tag = ? and loc = ?", new String[]{tag, loc + ""}, null, null, null);

        List<PointAera> aeras = new ArrayList<>();
        while (cursor.moveToNext()) {
            PointAera aera = new PointAera();
            int xindex = cursor.getColumnIndex("X");
            float x = cursor.getFloat(xindex);
            int yindex = cursor.getColumnIndex("Y");
            float y = cursor.getFloat(yindex);
            aera.setX(x);
            aera.setY(y);
            aeras.add(aera);
        }
        if (cursor != null) {
            cursor.close();
        }
        if (!aeras.isEmpty()) {
            if (px > aeras.get(0).getX() && px < aeras.get(1).getX() && py > aeras.get(0).getY() && py < aeras.get(1).getY()) {
                return true;
            }
        }
        return false;
    }

    public static NotePoint validPoint(List<NotePoint> notePoints) {
        notePoints = notePoints.subList(0, notePoints.size() / 2);
        NotePoint notePoint = new NotePoint();
        if (notePoints == null || notePoints.isEmpty()) {
            notePoint.setPX(0f);
            notePoint.setPY(0f);
            return notePoint;
        }
        int size = notePoints.size() > 10 ? 10 : notePoints.size();
        float[] pxs = new float[size];
        float[] pys = new float[size];
        float sumX = 0f;
        float sumY = 0f;
        for (int i = 0; i < size; i++) {
            int anInt = new Random().nextInt(notePoints.size());
            pxs[i] = notePoints.get(anInt).getPX();
            pys[i] = notePoints.get(anInt).getPY();
        }

        for (float px : pxs) {
            sumX += px;
        }
        for (float py : pys) {
            sumY += py;
        }

        notePoint.setPX(sumX / size);
        notePoint.setPY(sumY / size);
        return notePoint;
    }


    private static void generateDB() {
        Cursor cursor = sd.query("point_aera", null, null, null, null, null, null);

        for (int i = 0; cursor.moveToNext(); i++) {
            String tag = cursor.getString(cursor.getColumnIndex("TAG"));
            int loc = cursor.getInt(cursor.getColumnIndex("LOC"));
            float x = cursor.getFloat(cursor.getColumnIndex("X"));
            float y = cursor.getFloat(cursor.getColumnIndex("Y"));
            if (i % 2 != 0) {//奇数
                ContentValues contentValues = new ContentValues();
                contentValues.put("maxX", x);
                contentValues.put("maxY", y);
                int point_range = sd.update("point_range", contentValues, "tag=? and loc=?", new String[]{tag, loc + ""});
                Log.e("更新结果", point_range + "");
            } else { //偶数
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id", i / 2);
                contentValues.put("tag", tag);
                contentValues.put("loc", loc);
                contentValues.put("minX", x);
                contentValues.put("minY", y);
                sd.insert("point_range", null, contentValues);
            }
        }
    }

    public interface onDataChangedListener<E> {
        void onDataChanged(List<E> answer);
    }

    public static class Answer {
        public Answer(List<Integer> selects, List<String>... txts) {
            this.selects = selects;
            this.exe_one_result = txts[0];
            this.exe_two_result = txts[1];
            this.grammar_result = txts[2];
            this.translation_result = txts[3];
        }

        public List<String> exe_one_result;
        public List<String> exe_two_result;
        public List<String> grammar_result;
        public List<String> translation_result;
        public List<Integer> selects;
    }

    public enum CHAPTER {
        EXE_ONE, EXE_TWO, AUDITION, GRAMMAR, TRANSLATION;
    }*/
}