package by.vashkevich.teamwork2.mappers


interface Mapper<F, T> {

    fun map(from: F): T
}