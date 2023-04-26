package com.duoduo.mcqlogin.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.RemoteViews
import com.duoduo.mcqlogin.R
import com.duoduo.mcqlogin.api.ApiClient
import com.duoduo.mcqlogin.api.LoginService
import com.duoduo.mcqlogin.ui.MainActivity

class LoginWidgetProvider : AppWidgetProvider() {
    private val TAG = "WidgetProvider"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate ${appWidgetIds.contentToString()}")

        appWidgetIds.forEach { appWidgetId ->
            val views = RemoteViews(
                context.packageName,
                R.layout.widget_layout
            )
            views.apply {

                // click listeners
                setOnClickPendingIntent(
                    R.id.refresh,
                    PendingIntent.getService(
                        context,
                        0,
                        Intent(context, UpdateService::class.java).apply {
                            action = UpdateService.ACTION_UPDATE_SERVICE
                        },
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
                setOnClickPendingIntent(
                    R.id.login,
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, MainActivity::class.java),
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                )
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        Log.d(TAG, "onDeleted ${appWidgetIds.contentToString()}")
    }

    override fun onEnabled(context: Context?) {
        Log.d(TAG, "onEnabled")
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action == ACTION_UPDATE) {
            Log.d(TAG, "onReceive ACTION_UPDATE")

            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                ComponentName(
                    context.packageName,
                    LoginWidgetProvider::class.java.name
                )
            )
            Log.d(TAG, "appWidgetIds = ${appWidgetIds.toList()}")

            appWidgetIds.forEach { appWidgetId ->
                val views = RemoteViews(
                    context.packageName,
                    R.layout.widget_layout
                ).apply {
                    // update status
                    setTextViewText(
                        R.id.status,
                        intent.getStringExtra(UpdateService.EXTRA_STATUS)
                    )
                    setTextViewText(
                        R.id.description,
                        intent.getStringExtra(UpdateService.EXTRA_DESCRIPTION)
                    )
                    setImageViewResource(
                        R.id.img,
                        intent.getIntExtra(
                            UpdateService.EXTRA_ICON,
                            R.drawable.baseline_question_mark_24
                        )
                    )
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }

        }
    }

    companion object {
        const val ACTION_UPDATE = "com.duoduo.mcqlogin.UPDATE_WIDGET"
    }
}