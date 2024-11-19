package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask

class ClimateFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var climateId: Long? = this.intent.getLongExtra("cimateId", 0)

        if (climateId?.toInt() == 0) {
            climateId = null
        }

        PlantTask.getClimateForm(
            this,
            this.intent.getLongExtra("plantId", 0),
            climateId,
        )
    }

    override fun getId() = 0
}