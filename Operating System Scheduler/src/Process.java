public class Process //all times in seconds
{
    int num;
    int serviceTime;
    int diskTime;

    int processingTime;
    int birthTime;
    int deathTime;
    int CPUbegin;
    int finishTime;

    public Process(int num, int serviceTime, int diskTime)
    {
        this.num = num;
        this.serviceTime = serviceTime;
        this.diskTime = diskTime;

        processingTime = 0;
        birthTime = App.time;
        deathTime = -1;
        CPUbegin = -1;
        finishTime = -1;
    }

    public int calcTAT()
    {
        return deathTime - birthTime;
    }

    public double calcRatio()
    {
        return (double)(calcTAT()) / serviceTime;
    }

    public int calcResponseTime()
    {
        return CPUbegin - birthTime;
    }
}
