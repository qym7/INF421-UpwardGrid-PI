package jdg.graph;

import java.util.Comparator;
public class GridSegment_2 implements Comparable<GridSegment_2>
{
  public GridPoint_2 p, q;
  public double slope = 0;

  public GridSegment_2() {}
  
  public GridSegment_2(GridPoint_2 p, GridPoint_2 q) { this.p=p; this.q=q; }

  public GridPoint_2 source() {return this.p; }
  public GridPoint_2 target() {return this.q; }
  public GridPoint_2 vertex(int i) {
  	if(i==0)return this.p; 
  	else return this.q;	
  }
  
/**
 * returns the vector s.target() - s.source()
 */
  public GridVector_2 toVector() {
  	return new GridVector_2 (this.p,this.q);
  }

/**
 * returns a segment with source and target interchanged
 */
  public GridVector_2 opposite() {
  	return new GridVector_2 (this.q,this.p);
  }

/**
 * A point is on s, 
 * iff it is equal to the source or target of s, 
 * or if it is in the interior of s
 */  
  public boolean hasOn(GridPoint_2 point)
  {
      double length_pq = this.toVector().norm();
      double length_pc = (new GridVector_2(this.p, point)).norm();
      double length_cq = (new GridVector_2(point, this.q)).norm();

      return (length_pq == length_cq+length_pc);
  }

    /**
     * A point is IN s,
     * iff it is in the interior of s
     */
    /*
   public boolean hasIn(GridPoint_2 point)
   {
     if((point.x<this.p.x && point.x<this.q.x) || (point.x>this.p.x && point.x>this.q.x))
         return false;

       if((point.y<this.p.y && point.y<this.q.y) || (point.y>this.p.y && point.y>this.q.y))
           return false;

     if(point.equalsTo(this.p) || point.equalsTo(this.q))
       return false;

     double length_pq = this.toVector().norm();
     double length_pc = (new GridVector_2(this.p, point)).norm();
     double length_cq = (new GridVector_2(point, this.q)).norm();

      return (length_pq == length_cq+length_pc);
   }

     */


    public String toString() {return "["+p+","+q+"]"; }
  public int dimension() { return 2;}


  @Override
  public int compareTo(GridSegment_2 edge) {return Double.compare(this.slope, edge.slope);}
  
}




