var assocIndexOf=require("./_assocIndexOf");function listCacheGet(e){var s=this.__data__,a=assocIndexOf(s,e);return a<0?void 0:s[a][1]}module.exports=listCacheGet;