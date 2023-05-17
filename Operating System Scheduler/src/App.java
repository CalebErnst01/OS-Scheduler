import java.util.random.*;

public class App 
{
    static int timeout = 600; //set for 10min by default
    static int time = 0; //current time
    static int procNum = 0;

    public static void main(String[] args) throws Exception 
    {
        if(false)
            runFCFS();

        if(false)
            runRoundRobbin();

        if(false)
            runSRT();

        if(true)
            runHRRN();
    }

    //this method generates random Processes using a given procNum, RNG servicetime (4-12), & RNG disktime (0-2)
    private static Process generateProcess()
    {
        Process returner = new Process(procNum, 
                                        (int)(Math.random() *8) + 4,
                                        (int)(Math.random()*2)
                                        );

        procNum++;
        return returner;
    }

    private static void runFCFS()
    {
        FCFS algo = new FCFS();

        for(int i = 0; i < 4; i++)
            algo.scheduler.enQueue(generateProcess());

        while(time <= timeout)
        {
            if(algo.scheduler.length < 4) //make sure algo never runs out of processes
                algo.scheduler.enQueue(generateProcess());

            //making progress on current process
            algo.scheduler.front.p.processingTime++;


            if(algo.scheduler.front.p.processingTime >= algo.scheduler.front.p.serviceTime && algo.scheduler.front.p.finishTime == -1)
                algo.scheduler.front.p.finishTime = time;

            //if process is completed
            if(algo.scheduler.front.p.processingTime >= (algo.scheduler.front.p.serviceTime + algo.scheduler.front.p.diskTime) )
            {
                algo.finishedProcesses.enQueue(algo.scheduler.front.p);
                algo.finishedProcesses.end.p.deathTime = time;
                algo.scheduler.deQueue();
                
                algo.scheduler.front.p.CPUbegin = time -1; //to account for the time++ below
            }

            time++;
        }

        System.out.println("----- FIRST COME FIRST SERVE ------");
        algo.printReport();
    }

    private static void runRoundRobbin() 
    {
        FCFS RoundRobin = new FCFS();
        int quanta = 8; //change process after 5secs
        int qLimiter = 0;

        for(int i = 0; i < 4; i++)
            RoundRobin.scheduler.enQueue(generateProcess());

        while(time <= timeout)
        {
            if(RoundRobin.scheduler.length < 4)
                RoundRobin.scheduler.enQueue(generateProcess());

            RoundRobin.scheduler.front.p.processingTime++;
            qLimiter++;


            if(RoundRobin.scheduler.front.p.processingTime >= RoundRobin.scheduler.front.p.serviceTime && RoundRobin.scheduler.front.p.finishTime == -1)
                RoundRobin.scheduler.front.p.finishTime = time;


            if(RoundRobin.scheduler.front.p.processingTime >= (RoundRobin.scheduler.front.p.serviceTime + RoundRobin.scheduler.front.p.diskTime) )
            {
                RoundRobin.finishedProcesses.enQueue(RoundRobin.scheduler.front.p);
                RoundRobin.finishedProcesses.end.p.deathTime = time;
                RoundRobin.scheduler.deQueue();
                
                RoundRobin.scheduler.front.p.CPUbegin = time -1; //to account for the time++ below
                qLimiter = 0;
            }
            else if(qLimiter == quanta) //if process is not complete but has surpassed alloted time quanta
            {
                //take current process & push to back of queue
                Process shiftBack = RoundRobin.scheduler.front.p;
                RoundRobin.scheduler.deQueue();
                RoundRobin.scheduler.enQueue(shiftBack);
                App.time += shiftBack.diskTime;
                qLimiter = 0;
            }

            time++;
        }

        System.out.println("----- Round Robin ------");
        RoundRobin.printReport();
    }

    private static void runSRT() 
    {
        FCFS SRT = new FCFS();

        for(int i = 0; i < 4; i++)
        SRT.scheduler.enQueue(generateProcess());

            while(time <= timeout)
            {
                if(SRT.scheduler.length < 4)
                {
                    Process newGen = generateProcess();

                    //if newGen is smaller than smallest in scheduler
                    if( (SRT.scheduler.front.p.serviceTime + SRT.scheduler.front.p.diskTime) > (newGen.serviceTime + newGen.diskTime) )
                        SRT.scheduler.frontQueue(newGen);
                    else
                        SRT.scheduler.enQueue(newGen);
                }
                
                SRT.scheduler.front.p.processingTime++;


                if(SRT.scheduler.front.p.processingTime >= SRT.scheduler.front.p.serviceTime && SRT.scheduler.front.p.finishTime == -1)
                    SRT.scheduler.front.p.finishTime = time;

                //if process is completed
                if(SRT.scheduler.front.p.processingTime >= (SRT.scheduler.front.p.serviceTime + SRT.scheduler.front.p.diskTime) )
                {
                    SRT.finishedProcesses.enQueue(SRT.scheduler.front.p);
                    SRT.finishedProcesses.end.p.deathTime = time;
                    SRT.scheduler.deQueue();

                    //check for smallest process HERE
                    Process tiny = SRT.scheduler.findSmallestProcess();
                    SRT.scheduler.frontQueue(tiny);
                    SRT.scheduler.front.p.CPUbegin = time -1;
                }

                time++;
            }

        System.out.println("----- SHORTEST REMAINING TIME ------");
        SRT.printReport();
    }

    private static void runHRRN()
    {
        FCFS HRRN = new FCFS();

        for(int i = 0; i < 4; i++)
            HRRN.scheduler.enQueue(generateProcess());


            while(time <= timeout)
            {
                if(HRRN.scheduler.length < 4)
                {
                    Process newGen = generateProcess();

                    //if newGen's ratio is higher than highest response ratio
                    //wait time is calculated by time - birthTime
                    double newRatio = 1 + (App.time - newGen.birthTime) / (newGen.diskTime + newGen.serviceTime);
                    double highestRatio = 1 + (App.time - HRRN.scheduler.front.p.birthTime) / (HRRN.scheduler.front.p.diskTime + HRRN.scheduler.front.p.serviceTime);
                    if(newRatio > highestRatio)
                        HRRN.scheduler.frontQueue(newGen);
                    else
                        HRRN.scheduler.enQueue(newGen);
                }
                
                HRRN.scheduler.front.p.processingTime++;


                if(HRRN.scheduler.front.p.processingTime >= HRRN.scheduler.front.p.serviceTime && HRRN.scheduler.front.p.finishTime == -1)
                    HRRN.scheduler.front.p.finishTime = time;

                //if process is completed
                if(HRRN.scheduler.front.p.processingTime >= (HRRN.scheduler.front.p.serviceTime + HRRN.scheduler.front.p.diskTime) )
                {
                    HRRN.finishedProcesses.enQueue(HRRN.scheduler.front.p);
                    HRRN.finishedProcesses.end.p.deathTime = time;
                    HRRN.scheduler.deQueue();

                    //check for smallest process HERE
                    Process biggie = HRRN.scheduler.findLargestRatio();
                    HRRN.scheduler.frontQueue(biggie);
                    HRRN.scheduler.front.p.CPUbegin = time -1;
                }

                time++;
            }

        System.out.println("----- HIGHEST RESPONSE RATIO NEXT ------");
        HRRN.printReport();
    }
}
