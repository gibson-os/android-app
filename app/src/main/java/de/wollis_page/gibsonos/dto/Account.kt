package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.process.Process
import java.util.*

class Account(val account: Account) {
    private var processes: MutableList<Process>

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

    init {
        processes = ArrayList()
    }
}