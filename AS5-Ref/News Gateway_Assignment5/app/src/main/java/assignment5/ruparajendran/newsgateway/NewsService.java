package assignment5.ruparajendran.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class NewsService extends Service {

    private static final String TAG = "NewsService";
    private boolean running = true;
    private final ArrayList<Article> articleList = new ArrayList<>();
    private int count = 1;
    private ServiceReceiver serviceReceiver;

    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(MainActivity.ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);

        new Thread(new Runnable() {
            @Override
            public void run()
            {
                while (running)
                {
                    while(articleList.isEmpty())
                    {
                        try
                        {
                            Thread.sleep(250);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent();
                    intent.setAction(MainActivity.ACTION_NEWS_STORY);
                    intent.putExtra(MainActivity.ARTICLE_LIST, articleList);
                    sendBroadcast(intent);
                    articleList.clear();
                }
                Log.i(TAG, "NewsService was properly stopped");
            }
        }).start();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        running = false;
        unregisterReceiver(serviceReceiver);
        super.onDestroy();
    }

    public void setArticles(ArrayList<Article> articles)
    {
        articleList.clear();
        Log.d(TAG, "setArticles: bp: Number of Articles: " + articles.size());
        articleList.addAll(articles);
    }


    public void printLst(ArrayList<Article> tempList)          // for testing purpose
    {
        Log.d(TAG, "printLst: bp: Number of Articles: (Before)" + tempList.size());
        Log.d(TAG, "printLst: bp: =========================================================================================================");
        int i = 0;
        for(Article a : tempList)
        {
            Log.d(TAG, "printLst: bp: ---------------Article " + i++ + "---------------");
            Log.d(TAG, "printLst: bp: Title:" + a.getTitle());
            Log.d(TAG, "printLst: bp: Author:" + a.getAuthor());
            Log.d(TAG, "printLst: bp: Published on:" + a.getPublishedAt());
            Log.d(TAG, "printLst: bp: URL:" + a.getUrl());
            Log.d(TAG, "printLst: bp: Image URL:" + a.getUrltoImage());
            Log.d(TAG, "printLst: bp: Desc:" + a.getDescription());
            Log.d(TAG, "printLst: bp:---------------------------------------------");
        }
        Log.d(TAG, "printLst: bp: =========================================================================================================");
        Log.d(TAG, "printLst: bp: Number of Articles: (After)" + tempList.size());
    }

    class ServiceReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            switch (intent.getAction())
            {
                case MainActivity.ACTION_MSG_TO_SERVICE:
                    Sources temp = null;

                    if (intent.hasExtra(MainActivity.SOURCE))
                    {
                        temp = (Sources) intent.getSerializableExtra(MainActivity.SOURCE);
                    }
                    assert temp != null;
                    ArticleRunnable articleRunnable = new ArticleRunnable(NewsService.this,temp.getId());
                    new Thread(articleRunnable).start();
                    break;

            }
        }
    }



}

