package de.wollis_page.gibsonos.dto;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import de.wollis_page.gibsonos.process.Process;

public class Account
{
    @NonNull
    private final de.wollis_page.gibsonos.model.Account account;
    @NonNull
    private List<Process> processes;

    public Account(@NonNull de.wollis_page.gibsonos.model.Account account) {
        this.account = account;
        this.processes = new ArrayList<>();
    }

    @NonNull
    public de.wollis_page.gibsonos.model.Account getAccount() {
        return account;
    }

    @NonNull
    public List<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(@NonNull List<Process> processes) {
        this.processes = processes;
    }

    public void addProccess(@NonNull Process process) {
        this.processes.add(process);
    }

    public void remodeProccess(@NonNull Process process) {
        this.processes.remove(process);
    }
}
