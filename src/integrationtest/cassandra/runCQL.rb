require 'cassandra-cql'

db = CassandraCQL::Database.new(ARGV[0])

File.open(ARGV[1], "rb") { |f|
    cql = f.read
    cqlStatements = cql.split(";")
    cqlStatements.each { |cqlStatement|
        if (cqlStatement.length >1)
            db.execute(cqlStatement+";")
        end
    }
}