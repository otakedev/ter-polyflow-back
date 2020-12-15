abstract class Mapper<T> {
    
    static fieldEmpty (object: any, field: any) : boolean {
        if(!object.hasOwnProperty(field)) return false;
        if(object[field] == null) return false;
        if(object[field] == undefined) return false;
        if(object[field] === "") return false;
        return true;
    }

    static multipleFieldEmpty (object: any, fields: any[]) : boolean {
        return fields.map(e => Mapper.fieldEmpty(object, e)).includes(false)
    }
    
    abstract map(object: any) : T|null;
}

export default Mapper;