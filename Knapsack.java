class Knapsack {
  // Knapsack with memoization. Optimum-- exp'l time. 

int capacity=0; 
int nItems; 
int value[]; 
int weight[]; 
Item trackCombns []; 
boolean inTheBag[];
int maxVal=0;
int updateBagFor; 

class Item {
  int weight, value; 
}

Knapsack(int[] v, int[] w, int capty) {
  value=v;
  weight=w; 
  capacity=capty; 
  nItems=v.length;   
  trackCombns =new Item [pow2(nItems)]; 
}

//==========================

private int pow2(int k) { // returns 2^k
  if (k==0) return 1; 
  int r=1;
  for (int i=1; i<=k; i++) r=r << 1; 
  return r;
}

//==========================================

public boolean [] knapsack ()  {
	// Note: in trackCombns[] item i sits at trackCombns[2^i]. trackCombns[0] is of no use.  
		// item i sits in cell: i-1 of value/weight arrays. 

  // set the starters first
  int t=1; 
  for (int i=0; i<nItems; i++)  {
    Item item=new Item(); 
    item.value=value[i];
    item.weight=weight[i];
    trackCombns[t]=item;
    if (weight[i]<=capacity && value[i]>maxVal) { 
    	maxVal=value[i];  
    	updateBagFor=t; 
    }
    t=t<<1;
  }

  // now update-forward those-within-capacity
  for (int i=1; i<pow2(nItems-1); i++) 
    if ( trackCombns[i]!=null && trackCombns[i].weight<=capacity) // supposedly no 0-weight item-- but just in case. 
 	updateForward(i); 

  // for the other half-- just look to update maxVal if necessary---  w.o.updateFWDing. 
  for (int i=pow2(nItems-1); i<pow2(nItems); i++) {
    if ( trackCombns[i]!=null 
		&& trackCombns[i].weight<=capacity 
		&& trackCombns[i].value >maxVal) {
	    maxVal=trackCombns[i].value; 
	    updateBagFor=i; 
    }	
  }

  System.out.println("the max.value possible is "+maxVal+"\n\t handing in the sack");
  updateBag(updateBagFor);
  return inTheBag; 
}

//==========================================

private void updateForward(int x) {  
  // calculates the sack for every j in range where j=1(0^t)x, t=0..nItems-k-1. 
	// does this calculation by adding the weight/value to that of x-s. reads x from where it is(known already)
	// Note: at this point: the sack for i itself is calculated & is on trackComns[] earlier by another number before it. the loop above started it all.

  int k=getNBitsOn(x);
  int itemToBeAdded=1 << k-1; 
  for (int i=0; i<=nItems-k-1; i++) { // i:#0s in between. 
    Item item=new Item();
    itemToBeAdded = itemToBeAdded << 1; 
    item.value =value [i+k]+trackCombns[x].value;   
    item.weight=weight[i+k]+trackCombns[x].weight; 
    trackCombns[itemToBeAdded + x]=item;

    // see if a better sack & update if so
    if (item.weight<=capacity && item.value>maxVal) {
	    maxVal=item.value;   
	    updateBagFor=itemToBeAdded + x; 
    }  // if
  }  // for
} 

//==========================================

private void updateBag(int x) {  
  // puts those showing on x to the bag.
	// called when a current-max is found

  inTheBag=new boolean[nItems]; 

  int r=x; 
  int ind=0;
  while (r>0) {
    if (r!=(r>>1)<<1) inTheBag[ind]=true; 
    ind++;
    r=r>>1;
  }  //while
}


// ============================================

private int getNBitsOn(int x) { 
  // returns the #digits on the binary reprn. of x
  int t=0; // the bit-count
  while (x!=0) {x=x>>1; t++; }
  return t;
}

/* 
private int getNBitsOn(int x) {  
  // does the same

  return (""+Integer.toBinaryString(y)).length();
}
*/

//==========================================


private void printBig() {  // a helper 
  for (int i=0; i<pow2(nItems); i++)
    System.out.println("trackCombns["+i+"]: value: "
	+(trackCombns[i]==null?"-1":trackCombns[i].value)+
	"\tweight: "
	+(trackCombns[i]==null?-1:trackCombns[i].weight));
}

//==========================================


public static void main (String ags[]) {

 int [] v={3, 5, 2, 9, 6};  
 int [] w={2, 6, 2, 12, 7};  // 29
 
Knapsack ks = new Knapsack(v, w, 22);

boolean b[]=ks.knapsack();

System.out.print("\nItems : "); for (int i=0; i<ks.nItems; i++) if (b[i]) System.out.print(" "+i+"  ");
System.out.println("\n--------------------------------");
System.out.print("Value : "); for (int i=0; i<ks.nItems; i++) if (b[i]) System.out.print(" "+ks.value[i]+"  ");
System.out.println();
System.out.print("Weight: "); for (int i=0; i<ks.nItems; i++) if (b[i]) System.out.print(" "+ks.weight[i]+"  ");

}  // main 

}
