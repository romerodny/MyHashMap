package cop3530;

import cop3530.MyHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Assign5
{
    public static int hashf( String s, int x )
    {
        int result = 0;
        
        for( int i = 0; i < s.length( ); ++i )
            result = result * x + s.charAt( i );
        
        return result;
    }
    
    
    public static <KeyType,ValueType> void printDist( MyHashMap<KeyType,ValueType> m )
    {
        int [ ] count = m.getLengths( );
        
        for( int i = 0; i < count.length; ++i )
            if( count[ i ] != 0 )
                System.out.println( i + "=" + count[ i ] );
    }
    
    public static void main( String [ ] args )
    {
        basicMain( 31, 31 );
        basicMain( 31, 2 );
        basicMain( 31, 29 );
        basicMain( 31, 32 );
        basicMain( 31, 37 );
    }
    
    public static void basicMain( int mult1, int mult2 )
    {
        System.out.println( "*****************" );
        System.out.println( "USING " + mult1 + " and " + mult2 );
        
        MyHashMap<String,Integer> m1 = new MyHashMap<>(
                x -> hashf( x, mult1 ), x -> hashf( x, mult2 )
                 );
        
        Map<String,Integer> m2 = new HashMap<>( );
        
        List<String> arr = new ArrayList<>( );
        Random r = new Random( 3530 );
        
        int N = 2_000_000;
        int LEN = 9;
        char [ ] alpha = { 'a', 'b', 'c', 'd', 'e' };
        
        for( int i = 0; i < N; ++i )
        {
            String str = "";
            for( int j = 0; j < LEN; ++j )
                str += alpha[ r.nextInt( 5 ) ];
            
            arr.add( str );
        }
        
        long start, end;
        int count = 0;
        start = System.currentTimeMillis( );
        count = 0;
        for( String str : arr )
            m1.put( str, count++ );
        end = System.currentTimeMillis( );
        
        System.out.println( ( end - start ) + "ms  (me)" );
        
        start = System.currentTimeMillis( );
        count = 0;
        for( String str : arr )
            m2.put( str, count++ );
        end = System.currentTimeMillis( );
        System.out.println( ( end - start ) + "ms  (Java)" );
        
        System.out.println( m1.size( ) + " " + m2.size( ) );
        printDist( m1 );
        
        long total = 0;
        for( Map.Entry<String,Integer> e : m1 )
            total += e.getValue( );
        
        System.out.println( total );
        
        start = System.currentTimeMillis( );
        for( String str : arr )
            m1.remove( str );
        end = System.currentTimeMillis( );
        System.out.println( ( end - start ) + "ms  (me)" );
        
        
        start = System.currentTimeMillis( );
        for( String str : arr )
            m2.remove( str );
        end = System.currentTimeMillis( );
        System.out.println( ( end - start ) + "ms  (Java)" );
        
        System.out.println( m1.size( ) );
        
        System.out.println( "*****************" );
        System.out.println( );
    }
}
