package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.module.core.desktop.dto.App
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.process.Process
import kotlin.collections.ArrayList

class Account(val account: Account) {
    private var processes: MutableList<Process> = ArrayList()
    var apps: MutableList<App> = ArrayList()

    fun getProcesses(): List<Process> {
        return processes
    }

    fun setProcesses(processes: MutableList<Process>) {
        this.processes = processes
    }

    fun addProccess(process: Process) {
        processes.add(process)
    }

    fun removeProccess(process: Process) {
        processes.remove(process)
    }
}