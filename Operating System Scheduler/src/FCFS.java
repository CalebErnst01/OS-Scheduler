public class FCFS  //probably should've named this better, didn't realize at first that i'd reuse the entire thing for all 4 algorithms
{
    queue scheduler;
    
    //for performance criteria
    queue finishedProcesses;

    public FCFS()
    {
        scheduler = new queue();
        finishedProcesses = new queue();
    }

    public double calcThroughput() //# of completed processes / timeout
    {
        return finishedProcesses.length / (double)(App.timeout);
    }

    public void printReport()
    {
        System.out.println("Overall Throughput: " + calcThroughput());

        while(finishedProcesses.length > 0)
        {
            Process useMe = finishedProcesses.front.p;
            finishedProcesses.deQueue();

            System.out.println("-- Process #" + useMe.num + " --");
            System.out.println("Finish Time: " + useMe.finishTime + "sec(s)");
            System.out.println("Response Time: " + useMe.calcResponseTime() + "sec(s)");
            System.out.println("Turnaround Time: " + useMe.calcTAT() + "sec(s)");
            System.out.println("Ratio: " + useMe.calcRatio());
            System.out.println("-------------------------");
        }
    }

}
