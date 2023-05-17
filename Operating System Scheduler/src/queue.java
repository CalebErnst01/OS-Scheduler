public class queue 
{
    Node end;
    Node front;
    int length;

    public queue()
    {
        this.front = null;
        this.end = null;
        length = 0;
    }

    public void enQueue(Process insert)
    {
        Node temp = new Node(insert);
        length++;

        if(this.end == null)
        {
            this.front = this.end = temp;
            return;
        }
        else
        {
            this.end.next = temp;
            this.end = temp;
        }
    }

    public void deQueue()
    {
        if(this.front == null)
            return;

        this.front = this.front.next;

        if(this.front == null)
            this.end = null;

        length--;
    }

    //this is used by SRT & HRRN to shove a new element to front
    public void frontQueue(Process p)
    {
        Node ptr = front;

        //if very front is smallest element
        if(front.p.num == p.num)
            return;

        while(true)
        {   
            if(ptr.next != null)
            {
                if(ptr.next.p.num == p.num) //if next has same ID 
                {
                    if(ptr.next.next != null) //for mid element being removed
                    {
                        ptr.next = ptr.next.next;
                        break;
                    }
                    else
                    {
                        ptr.next = null; //for last element being removed
                        end = ptr;
                        break;
                    }
                }
                ptr = ptr.next;
            }
            else
            { //ik this is lazy, but i couldn't find out why some of the tiny's were dangling (BTW this is why the proc# count is so high compared to the others)
                break;
            }
        }

        //shove p to the very front
        Node temp = front;
        front = new Node(p);
        front.next = temp;
    }

    //used only by SRT
    public Process findSmallestProcess() //use front
    {
        int shortest = front.p.serviceTime + front.p.diskTime;
        Node seeker = front.next;
        Process returner = front.p;
        
        while(true)
        {
            int nextNode = seeker.p.diskTime + seeker.p.serviceTime; //ISSUE HERE!!!
            if(shortest >  nextNode)
            {
                shortest = nextNode;
                returner = seeker.p;
            }
            
            if(seeker.next == null)
                break;
            else
                seeker = seeker.next;
        }

        return returner;
    }

    //used only by HRRN; largly copied from above method
    public Process findLargestRatio()
    {
        double biggest = 1 + (App.time - front.p.birthTime) / (front.p.diskTime + front.p.serviceTime);
        Node seek = front.next;
        Process returner = front.p;

        while(true)
        {
            double nextRatio = 1 + (App.time - seek.p.birthTime) / (seek.p.diskTime + seek.p.serviceTime);
            if(biggest < nextRatio)
            {
                biggest = nextRatio;
                returner = seek.p;
            }

            if(seek.next == null)
                break;
            else
                seek = seek.next;
        }
        return returner;
    }
}
