package com.example.data

import kotlinx.coroutines.flow.Flow

class InquiryRepository(private val inquiryDao: InquiryDao) {
    val allInquiries: Flow<List<Inquiry>> = inquiryDao.getAllInquiries()

    suspend fun insert(inquiry: Inquiry) {
        inquiryDao.insertInquiry(inquiry)
    }

    suspend fun delete(id: Int) {
        inquiryDao.deleteInquiryById(id)
    }
}
