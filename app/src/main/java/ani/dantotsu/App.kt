package ani.dantotsu

import android.app.Activity
import android.app.Application
import android.os.Bundle

class App : Application() {

    private var currentActivity: Activity? = null

    inner class ActivityCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity
        }
        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity
        }
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }

    fun currentActivity(): Activity? = currentActivity

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(ActivityCallbacks())
    }
}
