package jp.yasuhiroki.enjoyandroid

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 */
class EnjoyAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        val workRequest =
            PeriodicWorkRequest.Builder(UpdateWidgetWorker::class.java, 15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "yasuhiroki",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        val oneTimeWorker = OneTimeWorkRequest.Builder(UpdateWidgetWorker::class.java).build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "yasuhiroki_first",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorker
        )
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {
        fun record(context: Context) {
            context.getSharedPreferences("yasuhiroki", Context.MODE_PRIVATE).apply {
                val records = getStringSet("work_history", mutableSetOf())!!.toMutableSet()
                edit().apply {
                    val currentTimeMillis = System.currentTimeMillis()
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedTime = sdf.format(Date(currentTimeMillis))
                    records.add(formattedTime)
                    putStringSet("work_history", records)
                    apply()
                }
            }
        }
    }

}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.enjoy_app_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class UpdateWidgetWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(applicationContext, EnjoyAppWidget::class.java)
        )

        // Update all widgets
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(applicationContext, appWidgetManager, appWidgetId)
        }

        EnjoyAppWidget.record(appContext)

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val currentTimeMillis = System.currentTimeMillis()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedTime = sdf.format(Date(currentTimeMillis))
        val widgetText = context.getString(R.string.appwidget_text) + "\n" + formattedTime
        val views = RemoteViews(context.packageName, R.layout.enjoy_app_widget)
        views.setTextViewText(R.id.appwidget_text, widgetText)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}