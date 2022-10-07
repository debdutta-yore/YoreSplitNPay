package co.yore.splitnpay.models

enum class TransactionType {
    Received,
    Paid,
    Unspecified
}

enum class TransactionStatus() {
    Started,
    Settled,
    AllSettled,
    Filtered
}