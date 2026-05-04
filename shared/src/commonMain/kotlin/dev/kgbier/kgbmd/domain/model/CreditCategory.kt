package dev.kgbier.kgbmd.domain.model

private fun String.toId(): CreditGroupingId = CreditGroupingId(this)

enum class CreditCategory(val groupingId: CreditGroupingId) {
    Director("amzn1.imdb.concept.name_credit_category.ace5cb4c-8708-4238-9542-04641e7c8171".toId()),
    Writer("amzn1.imdb.concept.name_credit_category.c84ecaff-add5-4f2e-81db-102a41881fe3".toId()),
    Actor("amzn1.imdb.concept.name_credit_category.a9ab2a8b-9153-4edb-a27a-7c2346830d77".toId()),
    Actress("amzn1.imdb.concept.name_credit_category.7f6d81aa-23aa-4503-844d-38201eb08761".toId()),
    Cast("amzn1.imdb.concept.name_credit_group.7caf7d16-5db9-4f4f-8864-d4c6e711c686".toId()),
    Producer("amzn1.imdb.concept.name_credit_category.0af123ce-1605-4a51-93cf-7ad477b11832".toId()),
    Composer("amzn1.imdb.concept.name_credit_category.00f5faa0-5f76-4eb5-87a1-ec8d484d1779".toId()),
    Cinematographer("amzn1.imdb.concept.name_credit_category.e2bf7217-c947-461b-aa58-47e27da1c78e".toId()),
    Editor("amzn1.imdb.concept.name_credit_category.63b1f9c6-9d3b-4be6-88fc-6321c9fa5ae2".toId()),
    CastingDirector("amzn1.imdb.concept.name_credit_category.67b6990c-f7de-4882-916b-dad87ec4406a".toId()),
    ProductionDesigner("amzn1.imdb.concept.name_credit_category.ce558628-5755-438c-92d7-757518864a00".toId()),
    SetDecorator("amzn1.imdb.concept.name_credit_category.359a8a76-1c15-4fd1-bd31-26c7e2a046f8".toId()),
    MakeupDepartment("amzn1.imdb.concept.name_credit_category.c3f25a53-cdb2-4260-ab78-1ef0a6052ee1".toId()),
    ProductionManagement("amzn1.imdb.concept.name_credit_category.b3eac1a6-a62b-4f46-8b93-5331b94f6af3".toId()),
    AssistantDirector("amzn1.imdb.concept.name_credit_category.306aa45f-30d6-4786-a7f9-b3103ebca806".toId()),
    ArtDepartment("amzn1.imdb.concept.name_credit_category.d782ea94-18fe-4513-a71c-7cdd652ef2d8".toId()),
    SoundDepartment("amzn1.imdb.concept.name_credit_category.ce258419-131b-41f0-b2c5-227f5d9b719f".toId()),
    SpecialEffects("amzn1.imdb.concept.name_credit_category.856eb49a-7610-47d5-9a07-c7df3f715075".toId()),
    CameraAndElectricalDepartment("amzn1.imdb.concept.name_credit_category.daf5bc70-1a41-413b-af5a-af1e285bf049".toId()),
    CastingDepartment("amzn1.imdb.concept.name_credit_category.5c60f6fa-29f1-40a4-8fe5-b4c26a6a7db4".toId()),
    CostumeAndWardrobeDepartment("amzn1.imdb.concept.name_credit_category.19e9f38d-89a0-4963-91c1-e3958f1d4534".toId()),
    EditorialDepartment("amzn1.imdb.concept.name_credit_category.2677b7dc-373c-41d1-8b13-7bdc171ca372".toId()),
    LocationManagement("amzn1.imdb.concept.name_credit_category.95338d09-b2a1-4188-ac9f-55f5255bb437".toId()),
    MusicDepartment("amzn1.imdb.concept.name_credit_category.aad1533c-6974-45a4-ba98-5f2f43286cfc".toId()),
    ScriptAndContinuityDepartment("amzn1.imdb.concept.name_credit_category.bf32c344-897c-4e0a-b401-36bfa2c3669e".toId()),
    TransportationDepartment("amzn1.imdb.concept.name_credit_category.8c952a79-f27c-4e5a-be85-b114b0ecd04e".toId()),
    Choreography("amzn1.imdb.concept.name_credit_category.d7346bfb-3f3f-4d38-ad4f-a9db4611290d".toId()),
    ColorDepartment("amzn1.imdb.concept.name_credit_category.ed26d7e5-8124-43d8-8280-bd818cf80873".toId()),
    CraftServices("amzn1.imdb.concept.name_credit_category.a7d0343f-d4c8-4684-8c82-590e070079ec".toId()),
    ProductionDepartment("amzn1.imdb.concept.name_credit_category.990a90e2-4761-41d9-bfd6-3bd90e122762".toId()),
    PropertyDepartment("amzn1.imdb.concept.name_credit_category.728a6705-9999-465a-abd7-2a89193d9f75".toId()),
    Publicity("amzn1.imdb.concept.name_credit_category.f49f3db9-3260-4a39-9741-9d27a5319d3c".toId()),
    AdditionalCrew("amzn1.imdb.concept.name_credit_category.a7c2d410-e513-4bd7-85d5-73060ec46a84".toId()),
    Thanks("amzn1.imdb.concept.name_credit_category.90de891d-6d5e-4711-9179-3eda18bd18e1".toId()),
    ;

    companion object {
        fun fromGroupingId(groupingId: CreditGroupingId): CreditCategory? =
            entries.firstOrNull { it.groupingId == groupingId }
    }
}
