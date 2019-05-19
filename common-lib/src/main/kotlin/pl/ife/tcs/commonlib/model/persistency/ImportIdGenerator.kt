package pl.ife.tcs.commonlib.model.persistency

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentityGenerator
import java.io.Serializable

class ImportIdGenerator: IdentityGenerator() {
    override fun generate(session: SharedSessionContractImplementor?, obj: Any?): Serializable {
        val id = session?.getEntityPersister(null, obj)?.classMetadata?.getIdentifier(obj, session)
        return id ?: super.generate(session, obj)
    }
}