package io.nambm.buildhabit.business.impl;

import io.nambm.buildhabit.business.TagBusiness;
import io.nambm.buildhabit.entity.TagEntity;
import io.nambm.buildhabit.model.tag.TagModel;
import org.springframework.stereotype.Service;

@Service
public class TagBusinessImpl extends GenericBusinessImpl<TagModel, TagEntity> implements TagBusiness {

}
