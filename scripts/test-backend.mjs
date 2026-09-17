import assert from 'node:assert/strict';
import {randomUUID} from 'node:crypto';
const base='http://127.0.0.1:9399/v1/projects/demo-gustto/locations/southamerica-east1/services/gustto';
async function call(path,body){const r=await fetch(base+path,{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(body)});const text=await r.text();try{return JSON.parse(text)}catch{throw new Error(text)}}
const raw=query=>call(':executeGraphql',{query});
const as=uid=>uid?{authClaims:{sub:uid,email:uid+'@example.test',email_verified:true,firebase:{sign_in_provider:'password'}}}:{unauthenticated:true};
const op=(name,variables={},uid=null,mutation=true)=>call('/connectors/app:impersonate'+(mutation?'Mutation':'Query'),{operationName:name,variables,extensions:{impersonate:as(uid)}});
function ok(r){assert.ok(!r.errors?.length&&!r.error&&!r.code,JSON.stringify(r));return r.data}
function denied(r){assert.ok(r.errors?.length||r.error||r.code,'Expected rejection: '+JSON.stringify(r))}
const id=randomUUID(),suffix=Date.now().toString(),user='user'+suffix,other='other'+suffix,admin='admin'+suffix;
ok(await raw(`mutation { city_upsert(data:{id:"LA_SERENA",name:"La Serena"}) establishmentType_upsert(data:{id:"Cafetería",name:"Cafetería"}) cuisine_upsert(data:{id:"Chilena",name:"Chilena"}) user_upsert(data:{id:"${admin}",username:"${admin}",email:"${admin}@example.test",role:"ADMIN"}) }`));
ok(await op('CreateProfile',{username:user},user));ok(await op('CreateProfile',{username:other},other));
assert.equal(ok(await op('MyProfile',{},user,false)).user.role,'USUARIO');
denied(await op('AdminRestaurants',{},user,false));denied(await op('AdminReviews',{},null,false));
const draft={id,name:'Prueba local',address:'Calle de prueba 1',cityId:'LA_SERENA',latitude:-29.9,longitude:-71.2,imageUrl:'https://example.test/image.jpg'};
denied(await op('SaveRestaurant',draft,user));ok(await op('SaveRestaurant',draft,admin));
denied(await op('SetRestaurantStatus',{id,status:'ACTIVO'},admin));
ok(await op('AssignType',{restaurantId:id,typeId:'Cafetería'},admin));ok(await op('AssignCuisine',{restaurantId:id,cuisineId:'Chilena'},admin));
for(let day=1;day<=7;day++)ok(await op('SaveSchedule',{restaurantId:id,day,opens:'12:00',closes:day===6?'00:30':'22:00',closed:day===1},admin));
denied(await op('SaveSchedule',{restaurantId:id,day:8,opens:'12:00',closes:'22:00',closed:false},admin));
ok(await op('SetRestaurantStatus',{id,status:'ACTIVO'},admin));
assert.ok(ok(await op('ListRestaurants',{},null,false)).restaurants.some(r=>r.id.replaceAll('-','')===id.replaceAll('-','')));
denied(await op('SaveReview',{restaurantId:id,rating:5,comment:'Bien'},null));
for(const rating of [0,6])denied(await op('SaveReview',{restaurantId:id,rating,comment:'Texto'},user));
ok(await op('SaveReview',{restaurantId:id,rating:1,comment:'No me gustó'},user));ok(await op('SaveReview',{restaurantId:id,rating:4,comment:'Actualizada'},user));
let place=ok(await op('ListRestaurants',{},null,false)).restaurants.find(r=>r.id.replaceAll('-','')===id.replaceAll('-',''));assert.equal(place.reviews_on_restaurant.length,1);assert.equal(place.reviews_on_restaurant[0].rating,4);
ok(await op('DeleteMyReview',{restaurantId:id},other));
place=ok(await op('ListRestaurants',{},null,false)).restaurants.find(r=>r.id.replaceAll('-','')===id.replaceAll('-',''));assert.equal(place.reviews_on_restaurant.length,1);
denied(await op('ModerateReview',{restaurantId:id,userId:user},other));
ok(await op('AddFavorite',{restaurantId:id},user));ok(await op('AddFavorite',{restaurantId:id},user));
assert.equal(ok(await op('MyFavorites',{},user,false)).favorites.length,1);assert.equal(ok(await op('MyFavorites',{},other,false)).favorites.length,0);
ok(await op('ModerateReview',{restaurantId:id,userId:user},admin));
ok(await op('SetRestaurantStatus',{id,status:'INACTIVO'},admin));
assert.ok(!ok(await op('ListRestaurants',{},null,false)).restaurants.some(r=>r.id.replaceAll('-','')===id.replaceAll('-','')));
assert.equal(ok(await op('MyFavorites',{},user,false)).favorites.length,0);
console.log('OK: roles, propiedad, rating, reseña única, favoritos únicos y privados, publicación completa, horarios, moderación e inactivos. Solo emulador local.');


