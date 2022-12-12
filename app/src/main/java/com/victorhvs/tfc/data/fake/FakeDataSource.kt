package com.victorhvs.tfc.data.fake

import com.victorhvs.tfc.domain.models.Stock
import com.victorhvs.tfc.domain.models.TimeSeries

object FakeDataSource {

    val flry3 = Stock(
        uuid = "FLRY3.SA",
        city = "São Paulo",
        country = "Brazil",
        description = "Fleury S.A., together with its subsidiaries, provides medical services in" +
                " the diagnostic, treatment, clinical analysis, health management, medical care," +
                " orthopedics, and ophthalmology areas in Brazil. The company operates through" +
                " three segments: Diagnostic Medicine, Integrated Medicine, and Dental. It" +
                " provides laboratory and image exam, diagnostic information, check-up and" +
                " reference laboratory, genomics, clinic day, center of infusions, dental" +
                " imaging exam, dental radiology, and diagnostic imaging services. The company" +
                " also offers Saúde I, a digital healthcare platform that provides services, " +
                "including teleconsultations, medical, diagnostic medicine exams, and " +
                "procedures; and corporate solutions and face-to-face primary care, as well " +
                "as solutions for physicians with digital office and medical education. It " +
                "operates 284 customer service units and 31 hospital-based units under the Labs" +
                " a+, Felippe Mattoso, Lafe, Fleury, a+SP, CIP, Moacir Cunha, Vita, inlab, " +
                "Weinmann, Serdil, a+ and Diagmax, a+, IRN / CPC, Diagnosis a+, a+BA, " +
                "and Pretti and Bioclinic brand names. The company was founded in 1926" +
                " and is headquartered in São Paulo, Brazil.",
        enabled = true,
        industry = "Diagnostics & Research",
        logoUrl = "https://logo.clearbit.com/ri.fleury.com.br",
        name = "FLEURY ON NM",
        price = 17.72,
        priceAbsoluteFloating = -0.24,
        priceFloating = 1.37,
        sector = "Healthcare",
        state = "SP",
        symbol = "FLRY3",
        website = "https://ri.fleury.com.br",
        week = listOf(16.91, 17.4, 17.16, 17.48, 17.72),
    )

    val timeSeries = TimeSeries(
        close = 1.0
    )

    val weekPrice =
        listOf(
            timeSeries.copy(),
            timeSeries.copy(close = 2.0, high = 3.0, low = 1.0, open = 1.0),
            timeSeries.copy(close = 5.0, high = 10.0, low = 3.0, open = 1.0),
            timeSeries.copy(close = 3.0, high = 10.0, low = 1.0, open = 6.0),
            timeSeries.copy(close = 1.0, high = 3.0, low = 1.0, open = 1.0),
            timeSeries.copy(close = 5.0, high = 3.0, low = 1.0, open = 1.0),
        )
}
